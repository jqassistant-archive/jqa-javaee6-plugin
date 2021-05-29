package com.buschmais.jqassistant.plugin.javaee6.test.scanner;

import java.io.IOException;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.ContainerFileResolver;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.scanner.JavaScope;
import com.buschmais.jqassistant.plugin.javaee6.api.scanner.WebApplicationScope;
import com.buschmais.jqassistant.plugin.javaee6.impl.scanner.WarClassesFileScannerPlugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class WarClassesResourceScannerPluginTest {

    @Mock
    private FileResource resource;

    @Mock
    private FileDescriptor fileDescriptor;

    @Mock
    private FileDescriptor containedFileDescriptor;

    @Mock
    private Scanner scanner;

    @Mock
    private ScannerContext scannerContext;

    @Mock
    private ContainerFileResolver containerFileResolver;

    @BeforeEach
    public void stub() {
        when(scanner.getContext()).thenReturn(scannerContext);
    }

    @Test
    public void accepts() throws IOException {
        WarClassesFileScannerPlugin plugin = new WarClassesFileScannerPlugin();
        assertThat(plugin.accepts(resource, "/Test.class", WebApplicationScope.WAR), equalTo(false));
        assertThat(plugin.accepts(resource, "/WEB-INF/classes/Test.class", WebApplicationScope.WAR), equalTo(true));
        assertThat(plugin.accepts(resource, "/WEB-INF/classes/Test.class", DefaultScope.NONE), equalTo(false));
    }

    @Test
    public void scan() throws IOException {
        when(scannerContext.getCurrentDescriptor()).thenReturn(fileDescriptor);
        when(scanner.scan(resource, fileDescriptor, "/Test.class", JavaScope.CLASSPATH)).thenReturn(containedFileDescriptor);

        WarClassesFileScannerPlugin plugin = new WarClassesFileScannerPlugin();
        FileDescriptor scan = plugin.scan(resource, "/WEB-INF/classes/Test.class", WebApplicationScope.WAR, scanner);

        assertThat(scan, is(containedFileDescriptor));
        verify(scanner).scan(resource, fileDescriptor, "/Test.class", JavaScope.CLASSPATH);
    }

}
