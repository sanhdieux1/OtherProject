package test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.khoa.ultils.Ultils;

public class UltilsTest {
    
    @Test
    public void testGetName(){
        System.out.println(Ultils.getName("test.doc"));
        assertEquals("test", Ultils.getName("test.doc")) ;
    }
}
