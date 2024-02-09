#include "basis.h"

int main(int argc, char **argv) {
  int *arr = (int *)malloc(sizeof(int) * argc - 1);
  parse_variable(argv, arr, argc);
  insertion_sort(arr, argc-1);
  print_array(arr, argc-1);
  return 0;
}
