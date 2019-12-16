package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<String> future;
    @BeforeEach
    public void setUp(){
        future = new Future<String>();
    }

    @Test
    public void gettest() throws InterruptedException {
        String result = future.get();
        assertEquals(null,result);
    }

    @Test
    public void gettimeouttest(){
        assertNull(future.get(100, TimeUnit.MILLISECONDS));
    }

    @Test
    public void gettimeouttest1(){
        future.resolve("Done");
        assertNotEquals(null,(future.get(100, TimeUnit.MILLISECONDS)));
    }

    @Test
    public void isDonetest1(){
        assertFalse(future.isDone());
    }

    @Test
    public void resolvetest() throws InterruptedException {
        future.resolve("Done");
        String result = future.get();
        assertNotEquals(null,result);
        assertEquals("Done",result);
    }

    @Test
    public void isDonetest2() throws InterruptedException {
        resolvetest();
        future.resolve("Done");
        assertTrue(future.isDone());
    }



}
