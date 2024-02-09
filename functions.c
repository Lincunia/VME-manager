#include "basis.h"

void insertion_sort(int arr[], int n) {
  int i, key, j;
  for (i = 1; i < n; i++) {
    key = arr[i];
    j = i - 1;
    while (j >= 0 && arr[j] > key) {
      arr[j + 1] = arr[j];
      j = j - 1;
    }
    arr[j + 1] = key;
  }
}

void print_array(int arr[], int n) {
  int i;
  for (i = 0; i < n; i++)
    printf("%d ", arr[i]);
  printf("\n");
}

void parse_variable(char **argv, int arr[], int n){
  int i;
  for (i = 1; i < n; i++)
    *(arr+(i-1)) = atoi(argv[i]);
}
