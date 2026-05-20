package com.baldursgate3.trainer;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides low-level memory scanning and patching for the Baldur's Gate 3 process.
 * Uses JNA to interface with Windows kernel32.dll for memory operations.
 */
public class MemoryScanner {

    private static final Logger logger = LoggerFactory.getLogger(MemoryScanner.class);

    private final int processId;
    private final WinNT.HANDLE processHandle;

    /**
     * Creates a MemoryScanner attached to the given process ID.
     *
     * @param processId the PID of the BG3 process
     */
    public MemoryScanner(int processId) {
        this.processId = processId;
        this.processHandle = Kernel32.INSTANCE.OpenProcess(
                WinNT.PROCESS_VM_READ | WinNT.PROCESS_VM_WRITE | WinNT.PROCESS_VM_OPERATION,
                false,
                processId
        );
        if (processHandle == null) {
            throw new RuntimeException("Failed to open process: " + Native.getLastError());
        }
        logger.info("Attached to process {}", processId);
    }

    /**
     * Scans the process memory for a specific byte pattern.
     *
     * @param pattern the byte array to search for
     * @param moduleName optional module to restrict search (e.g., "bg3.exe")
     * @return list of addresses where pattern was found
     */
    public List<Long> findPattern(byte[] pattern, String moduleName) {
        List<Long> results = new ArrayList<>();
        // In a real implementation, this would enumerate memory regions
        // and search each for the pattern. Simplified for demonstration.
        long[] addresses = {0x7FFA12345678L, 0x7FFA87654321L}; // Dummy addresses
        for (long addr : addresses) {
            byte[] buffer = new byte[pattern.length];
            IntByReference bytesRead = new IntByReference();
            boolean success = Kernel32.INSTANCE.ReadProcessMemory(
                    processHandle,
                    addr,
                    buffer,
                    buffer.length,
                    bytesRead
            );
            if (success && bytesRead.getValue() == pattern.length) {
                boolean match = true;
                for (int i = 0; i < pattern.length; i++) {
                    if (buffer[i] != pattern[i]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    results.add(addr);
                }
            }
        }
        logger.debug("Found {} matches for pattern", results.size());
        return results;
    }

    /**
     * Writes a byte array to a specific memory address.
     *
     * @param address the target address
     * @param data    the bytes to write
     * @return true if successful
     */
    public boolean writeMemory(long address, byte[] data) {
        IntByReference bytesWritten = new IntByReference();
        boolean success = Kernel32.INSTANCE.WriteProcessMemory(
                processHandle,
                address,
                data,
                data.length,
                bytesWritten
        );
        if (success && bytesWritten.getValue() == data.length) {
            logger.info("Wrote {} bytes to address 0x{}", data.length, Long.toHexString(address));
            return true;
        } else {
            logger.error("Failed to write memory at 0x{}", Long.toHexString(address));
            return false;
        }
    }

    /**
     * Closes the process handle to free resources.
     */
    public void close() {
        if (processHandle != null) {
            Kernel32.INSTANCE.CloseHandle(processHandle);
            logger.info("Closed process handle");
        }
    }
}
