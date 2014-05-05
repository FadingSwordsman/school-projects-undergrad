CC = gcc
CFLAGS = -Wall

TARGETS = ipc_mp_bench ipc_shmem_bench

all:	$(TARGETS)

clean: rm *.o

ipc_mp_bench: ipc_mp_bench.c
	$(CC) $(CFLAGS) ipc_mp_bench.c

ipc_shmem_bench: ipc_shmem_bench.c
	$(CC) $(CFLAGS) ipc_shmem_bench.c
