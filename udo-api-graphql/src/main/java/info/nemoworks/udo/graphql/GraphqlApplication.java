//package info.nemoworks.udo.graphql;
//
//import info.nemoworks.udo.messaging.HTTPServiceGateway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//
//@SpringBootApplication
//@ComponentScan(basePackages = "info.nemoworks.udo")
//public class GraphqlApplication implements CommandLineRunner {
//
//    public static void main(String[] args) {
//        SpringApplication.run(GraphqlApplication.class, args);
//    }
//
//    @Autowired
//    private HTTPServiceGateway httpServiceGateway;
//
//    @Override
//    public void run(String... args) throws Exception {
//        while(true){
//            System.out.println("start...");
//            if(httpServiceGateway.getEndpoints().size()>0){
//                httpServiceGateway.start();
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}
