package learn_rxjava;

import static org.junit.Assert.*;
import static java.lang.System.*;
import static java.util.Arrays.asList;

import io.reactivex.*;
import java.util.*;
import org.junit.*;

public class FlowsTest {

    @Test
    public void test_doSquares() {
        List squares = Flows.doSquares();
        
        out.println("squares = " + squares);
        
        assertEquals(64, squares.size());
        assertEquals(asList(1, 4, 9, 16, 25, 36, 49, 64, 81, 100), 
            squares.subList(0, 10));
    }
}
