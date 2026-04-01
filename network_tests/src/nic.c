/*
 * COMPILACIÓN DE WINDOWS: gcc nic.c -liphlpapi -lws2_32 -o nic.exe
 * COMPILACIÓN DE OTRO OS: gcc nic.c -o nic.out
 */
#include <stdio.h>

#ifdef _WIN32

#include <iphlpapi.h>
#include <winsock2.h>
#pragma comment(lib, "iphlpapi.lib")
#pragma comment(lib, "ws2_32.lib")

#else

#include <arpa/inet.h>
#include <ifaddrs.h>
#include <netinet/in.h>

#endif

// #include <sys/socket.h>
// #include <netdb.h>
// #include <string.h>

void get_interfaces()
{
#ifdef _WIN32
    // Inicializar Winsock
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);
    // Inicilizar busqueda de adaptadores
    PIP_ADAPTER_INFO pAdapterInfo;
    PIP_ADAPTER_INFO pAdapter = NULL;
    DWORD dwSize = sizeof(IP_ADAPTER_INFO);

    pAdapterInfo = (IP_ADAPTER_INFO*)malloc(sizeof(IP_ADAPTER_INFO));
    if (pAdapterInfo == NULL)
        return;

    // Obtener el tamaño necesario
    if (GetAdaptersInfo(pAdapterInfo, &dwSize) == ERROR_BUFFER_OVERFLOW) {
        free(pAdapterInfo);
        pAdapterInfo = (IP_ADAPTER_INFO*)malloc(dwSize);
        if (pAdapterInfo == NULL)
            return;
    }

    if (GetAdaptersInfo(pAdapterInfo, &dwSize) == NO_ERROR) {
        pAdapter = pAdapterInfo;
        while (pAdapter) {
            printf("Interfaz: %s\n", pAdapter->Description);
            printf("  IP: %s\n", pAdapter->IpAddressList.IpAddress.String);
            printf("  MAC: %02X-%02X-%02X-%02X-%02X-%02X\n",
                pAdapter->Address[0], pAdapter->Address[1],
                pAdapter->Address[2], pAdapter->Address[3],
                pAdapter->Address[4], pAdapter->Address[5]);

            pAdapter = pAdapter->Next;
        }
    }

    free(pAdapterInfo);
#else

    struct ifaddrs *interfaces, *ifa;
    char ip[INET6_ADDRSTRLEN];

    if (getifaddrs(&interfaces) == -1) {
        perror("getifaddrs: ");
        return;
    }

    for (ifa = interfaces; ifa != NULL; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr == NULL) {
            continue;
        }

        if (ifa->ifa_addr->sa_family == AF_INET) {
            struct sockaddr_in* addr_v4 = (struct sockaddr_in*)ifa->ifa_addr;
            inet_ntop(AF_INET, &addr_v4->sin_addr, ip, sizeof(ip));
            printf(
                "Interfaz (IPV4): %s\n"
                "\tIP: %s\n",
                ifa->ifa_name, ip);
            continue;
        }
        if (ifa->ifa_addr->sa_family == AF_INET6) {
            struct sockaddr_in6* addr_v6 = (struct sockaddr_in6*)ifa->ifa_addr;
            inet_ntop(AF_INET6, &addr_v6->sin6_addr, ip, sizeof(ip));
            printf(
                "Interfaz (IPV6): %s\n"
                "\tIP: %s\n",
                ifa->ifa_name, ip);
            continue;
        }
    }

    freeifaddrs(interfaces);
#endif
}

int main(void)
{

    get_interfaces();
    return 0;
}
