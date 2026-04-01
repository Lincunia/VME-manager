#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <pthread.h>

#define MAX_THREADS 50
#define TIMEOUT_SEC 1

typedef struct {
    int start_port;
    int end_port;
    char ip_address[16];
} scan_params_t;

typedef struct {
    int port;
    int status; // 0: disponible, 1: ocupado, -1: error
} port_result_t;

port_result_t results[65536];
int total_ports_scanned = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

// Función para verificar puerto con timeout
int check_port_with_timeout(const char *ip, int port, int timeout_sec) {
    int sock;
    struct sockaddr_in addr;
    struct timeval timeout;
    fd_set fdset;
    
    // Crear socket
    sock = socket(AF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        return -1;
    }
    
    // Configurar socket como no bloqueante
    int flags = fcntl(sock, F_GETFL, 0);
    fcntl(sock, F_SETFL, flags | O_NONBLOCK);
    
    // Configurar dirección
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    inet_pton(AF_INET, ip, &addr.sin_addr);
    
    // Intentar conectar
    connect(sock, (struct sockaddr *)&addr, sizeof(addr));
    
    // Configurar select para timeout
    FD_ZERO(&fdset);
    FD_SET(sock, &fdset);
    timeout.tv_sec = timeout_sec;
    timeout.tv_usec = 0;
    
    int result = select(sock + 1, NULL, &fdset, NULL, &timeout);
    
    if (result <= 0) {
        close(sock);
        return 0; // Timeout o error - puerto disponible
    }
    
    // Verificar si hay error en el socket
    int so_error;
    socklen_t len = sizeof(so_error);
    getsockopt(sock, SOL_SOCKET, SO_ERROR, &so_error, &len);
    
    close(sock);
    
    if (so_error == 0) {
        return 1; // Puerto ocupado
    }
    
    return 0; // Puerto disponible
}

// Función para hilo de escaneo
void *scan_thread(void *arg) {
    scan_params_t *params = (scan_params_t *)arg;
    
    for (int port = params->start_port; port <= params->end_port; port++) {
        int status = check_port_with_timeout(params->ip_address, port, TIMEOUT_SEC);
        
        pthread_mutex_lock(&mutex);
        results[port].port = port;
        results[port].status = status;
        total_ports_scanned++;
        pthread_mutex_unlock(&mutex);
    }
    
    return NULL;
}
//
// Escaneo paralelo de puertos
void parallel_port_scan(const char *ip, int start_port, int end_port) {
    int total_ports = end_port - start_port + 1;
    int ports_per_thread = total_ports / MAX_THREADS;
    pthread_t threads[MAX_THREADS];
    scan_params_t params[MAX_THREADS];
    
    printf("Iniciando escaneo paralelo de %d puertos...\n", total_ports);
    printf("Usando %d hilos\n", MAX_THREADS);
    
    // Crear hilos
    for (int i = 0; i < MAX_THREADS; i++) {
        params[i].start_port = start_port + (i * ports_per_thread);
        params[i].end_port = (i == MAX_THREADS - 1) ? end_port : 
                              params[i].start_port + ports_per_thread - 1;
        strcpy(params[i].ip_address, ip);
        
        if (pthread_create(&threads[i], NULL, scan_thread, &params[i]) != 0) {
            perror("Error creando hilo");
            exit(1);
        }
    }
    
    // Esperar hilos y mostrar progreso
    int last_progress = 0;
    while (1) {
        pthread_mutex_lock(&mutex);
        int progress = (total_ports_scanned * 100) / total_ports;
        pthread_mutex_unlock(&mutex);
        
        if (progress > last_progress) {
            printf("Progreso: %d%%\r", progress);
            fflush(stdout);
            last_progress = progress;
        }
        
        if (total_ports_scanned >= total_ports) {
            break;
        }
        usleep(100000); // 100ms
    }
    
    // Esperar todos los hilos
    for (int i = 0; i < MAX_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }
    
    for (int port = start_port; port <= end_port; port++) {
        if (results[port].status == 0) {
			continue;
        }
		printf("Puerto %d: con status %d\n", port, results[port].status);
    }
}

int main() {
    int option;
    do {
        printf("\nMENÚ PRINCIPAL:\n");
        printf("1. Escaneo completo (1-65535)\n");
        printf("2. Escaneo personalizado\n");
        printf("3. Verificar puerto específico\n");
        printf("0. Salir\n");
        printf("Selecciona una opción: ");
        scanf("%d", &option);
        
        switch(option) {
            case 1:
                printf("Esto puede tomar varios minutos...\n");
                parallel_port_scan("127.0.0.1", 1, 65535);
                break;
                
            case 2: {
                int start, end;
                char ip[16];
                printf("Ingresa IP a escanear (ej: 127.0.0.1): ");
                scanf("%s", ip);
                printf("Puerto inicial: ");
                scanf("%d", &start);
                printf("Puerto final: ");
                scanf("%d", &end);
                parallel_port_scan(ip, start, end);
                break;
            }
                
            case 3: {
                int port;
                printf("Ingresa el puerto a verificar: ");
                scanf("%d", &port);
                int result = check_port_with_timeout("127.0.0.1", port, TIMEOUT_SEC);
                if (result == 1) {
                    printf("Puerto %d: OCUPADO\n", port);
                } else if (result == 0) {
                    printf("Puerto %d: DISPONIBLE\n", port);
                } else {
                    printf("Error verificando puerto %d\n", port);
                }
                break;
            }
                
            case 0:
                printf("Saliendo...\n");
                break;
                
            default:
                printf("Opción no válida\n");
        }
        
    } while (option != 0);
    
    return 0;
}
