CC = gcc
SHMEM = -lpthread -lm
CFLAGS = -Wall

TARGETS = ipc_mp_bench ipc_shmem_bench test_read test_fread test_memcpy io_bench

all:	$(TARGETS)

clean:
	rm ipc_mp_bench ipc_shmem_bench test_fread test_read test_memcpy

ipc_mp_bench: ipc_mp_bench.c
	$(CC) $(CFLAGS) -o ipc_mp_bench ipc_mp_bench.c

ipc_shmem_bench: ipc_shmem_bench.c
	$(CC) $(CFLAGS) -o ipc_shmem_bench ipc_shmem_bench.c $(SHMEM)

test_read: test_read.c
	$(CC) $(CFLAGS) -o test_read test_read.c

test_fread: test_fread.c
	$(CC) $(CFLAGS) -o test_fread test_fread.c

test_memcpy: test_memcpy.c
	$(CC) $(CFLAGS) -o test_memcpy test_memcpy.c

io_bench: io_bench.c
	$(CC) $(CFLAGS) -o io_bench io_bench.c
