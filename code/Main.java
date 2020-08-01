/**
 * Main function for test
 */
public class Main {
    public static void main(String[] args) {
//        Algorithm algorithm = new Algorithm();
//        algorithm.insertData();
//
//        System.out.println("Insert Data Successfully!");

        Recommendation recommendation = new Recommendation();
        System.out.println("[쇼핑몰 추천]");

        //have to put gender value for running recommendation function
        recommendation.selectShopClothes("여자");

        System.out.println("[사용자옷 추천]");
        recommendation.selectUserClothes("suin");
    }
}
