import service.PlannerService;

public class Application {
    public static void main(String[] args) {
        PlannerService plannerService = new PlannerService();
        plannerService.start();
    }
}