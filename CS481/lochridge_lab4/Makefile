CC = gcc
SHMEM = -lpthread -lm
CFLAGS = -Wall

TARGETS = ipc_mp_bench ipc_shmem_bench io_bench

all:	$(TARGETS)

clean:
	rm ipc_mp_bench ipc_shmem_bench io_bench

ipc_mp_bench: ipc_mp_bench.c
	$(CC) $(CFLAGS) -o ipc_mp_bench ipc_mp_bench.c

ipc_shmem_bench: ipc_shmem_bench.c
	$(CC) $(CFLAGS) -o ipc_shmem_bench ipc_shmem_bench.c $(SHMEM)

io_bench: io_bench.c
	$(CC) $(CFLAGS) -o io_bench io_bench.c
