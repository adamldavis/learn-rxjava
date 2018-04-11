package learn_rxjava;

import static java.lang.System.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import io.reactivex.*;

import java.io.File;
import java.util.*;

public class FlowsTest {

    @Test
    public void test_doSquares() {
        List squares = Flows.doSquares();
        
        out.println("squares = " + squares);
        
        assertEquals(64, squares.size());
        assertEquals(asList(1, 4, 9, 16, 25, 36, 49, 64, 81, 100), 
            squares.subList(0, 10));
    }

    @RepeatedTest(2)
    public void test_writeFile_readFile() {
        File f = new File("test");
        Flows.writeFile(f);
        Flows.readFile(f);
    }

    @RepeatedTest(2)
    public void test_writeFile_readFileWithProcessor() {
        File f = new File("test");
        Flows.writeFile(f);
        List list = Flows.readFileWithProcessor(f);
        assertEquals(100, list.size());
    }
}
