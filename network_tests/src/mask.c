#include <stdio.h>

#ifdef _WIN32

#include <winsock2.h>
#include <iphlpapi.h>
#include <stdio.h>
#pragma comment(lib, "iphlpapi.lib")
#pragma comment(lib, "ws2_32.lib")

#else

#include <arpa/inet.h>
#include <ifaddrs.h>

#endif

void get_net_mask()
{
#ifdef _WIN32
    // Inicializar Winsock
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

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
            printf("  Máscara: %s\n", pAdapter->IpAddressList.IpMask.String);

            // Calcular red
            unsigned long ip = inet_addr(pAdapter->IpAddressList.IpAddress.String);
            unsigned long mask = inet_addr(pAdapter->IpAddressList.IpMask.String);
            unsigned long red = ip & mask;

            struct in_addr red_addr;
            red_addr.s_addr = red;
            printf("  Red: %s\n", inet_ntoa(red_addr));

            pAdapter = pAdapter->Next;
        }
    }

    free(pAdapterInfo);
    WSACleanup();
#else
    struct ifaddrs *interfaces, *ifa;
    char ip[INET_ADDRSTRLEN];
    char mascara[INET_ADDRSTRLEN];

    if (getifaddrs(&interfaces) == -1) {
        perror("getifaddrs");
        return;
    }

    for (ifa = interfaces; ifa != NULL; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr == NULL || ifa->ifa_netmask == NULL) {
            continue;
        }

        // Solo interfaces IPv4
        if (ifa->ifa_addr->sa_family == AF_INET) {
            struct sockaddr_in* addr = (struct sockaddr_in*)ifa->ifa_addr;
            struct sockaddr_in* netmask = (struct sockaddr_in*)ifa->ifa_netmask;

            inet_ntop(AF_INET, &addr->sin_addr, ip, sizeof(ip));
            inet_ntop(AF_INET, &netmask->sin_addr, mascara, sizeof(mascara));

            printf("Interfaz: %s\n", ifa->ifa_name);
            printf("\tIP: %s\n", ip);
            printf("\tMáscara: %s\n", mascara);

            // Calcular y mostrar la red
            unsigned long ip_red = ntohl(addr->sin_addr.s_addr) & ntohl(netmask->sin_addr.s_addr);
            struct in_addr red;
            red.s_addr = htonl(ip_red);
            printf("\tRed: %s\n", inet_ntoa(red));
        }
    }

    freeifaddrs(interfaces);
#endif
}

int main(void)
{
    get_net_mask();
    return 0;
}
