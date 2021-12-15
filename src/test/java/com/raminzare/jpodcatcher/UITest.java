package com.raminzare.jpodcatcher;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

class UITest {


    @Test
    void testUI_if_windows(){
        Assumptions.assumeTrue(()-> System.getProperty("os.name").startsWith("Windows") ,
                "This test will run only on windows");
        System.out.println("TEST IS RUNNING");
    }

    @Test
    void testUI_if_not_windows(){
        Assumptions.assumeFalse(()-> System.getProperty("os.name").startsWith("Windows") ,
                "This test will run only on windows");
        System.out.println("TEST IS RUNNING");
    }

    @Test
    void test_project(){
        Config config = new Config();
        config.setConf1(true);
        Assumptions.assumingThat(()-> !System.getProperty("java.vm.specification.version").equals("17")
                ,()->{
                    config.setConf2(true);
                });
        config.setConf3(true);

        System.out.println(config);
    }


    class Config{
        boolean conf1, conf2, conf3;

        public void setConf1(boolean conf1) {
            this.conf1 = conf1;
        }

        public void setConf2(boolean conf2) {
            this.conf2 = conf2;
        }

        public void setConf3(boolean conf3) {
            this.conf3 = conf3;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "conf1=" + conf1 +
                    ", conf2=" + conf2 +
                    ", conf3=" + conf3 +
                    '}';
        }
    }
}
