
package lapr1_projeto_omen;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;


public class LAPR1_PROJETO_OMENTest {
    
    public LAPR1_PROJETO_OMENTest() {
    }

    @Test
    public void testLerFicheiro() throws Exception {
        System.out.println("lerFicheiro");
        File file = new File("DAYTON.csv");
        LAPR1_PROJETO_OMEN.lerFicheiro(file);
    }

    @Test
    public void testContarDados() throws Exception {
        System.out.println("contarDados");
        File file = new File("DAYTON.csv");
        int expResult = 22680;
        int result = LAPR1_PROJETO_OMEN.contarDados(file);
        assertEquals(expResult, result);
    }

    @Test
    public void testVerificarLinha() {
        System.out.println("verificarLinha");
        String linha = "";
        boolean expResult = false;
        boolean result = LAPR1_PROJETO_OMEN.verificarLinha(linha);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testVerificarLinha2() {
        System.out.println("verificarLinha");
        String linha = "2016-01-01 00:00:00,1793";
        boolean expResult = true;
        boolean result = LAPR1_PROJETO_OMEN.verificarLinha(linha);
        assertEquals(expResult, result);
    }
    
}
