#include <arpa/inet.h>
#include <ifaddrs.h>
#include <string.h>

// Convertir máscara a CIDR
int mascara_a_cidr(const char *mascara_str) {
    struct in_addr addr;
    inet_pton(AF_INET, mascara_str, &addr);
    unsigned long m = ntohl(addr.s_addr);
    int cidr = 0;
    
    while (m & 0x80000000) {
        cidr++;
        m <<= 1;
    }
    return cidr;
}

// Convertir CIDR a máscara
void cidr_a_mascara(int cidr, char *mascara_str) {
    unsigned long mask = 0;
    for (int i = 0; i < cidr; i++) {
        mask |= (1 << (31 - i));
    }
    mask = htonl(mask);
    
    struct in_addr addr;
    addr.s_addr = mask;
    strcpy(mascara_str, inet_ntoa(addr));
}

// Verificar si dos IPs están en la misma red
int misma_red(const char *ip1, const char *ip2, const char *mascara) {
    struct in_addr addr1, addr2, mask;
    
    inet_pton(AF_INET, ip1, &addr1);
    inet_pton(AF_INET, ip2, &addr2);
    inet_pton(AF_INET, mascara, &mask);
    
    return ((addr1.s_addr & mask.s_addr) == (addr2.s_addr & mask.s_addr));
}
