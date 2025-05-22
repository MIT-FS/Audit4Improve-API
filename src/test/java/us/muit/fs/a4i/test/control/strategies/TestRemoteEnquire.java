import static org.junit.jupiter.api.Assertions.*; //JUnit methods
import org.junit.jupiter.api.Test; //to indicate that is a test
import java.util.List;

public class RemoteEnquirerTest {

    @Test
    public void testGetPullRequests_basic() throws Exception {
        RemoteEnquirer enquirer = new RemoteEnquirer("owner", "repo", "token"); //to get information about the repository

        List<PullRequest> pullRequests = enquirer.getPullRequests(); //this calls the method to get the list of PUllRequest

        assertNotNull(pullRequests, "the list cannot be null");  //control that the list is not null unless it gives the error message

        // assertFalse(pullRequests.isEmpty(), "the list cannot be empty"); //this is to control that the list is not empty, only if we always want that at least one pull request is done

        for (PullRequest pr : pullRequests) {
            assertNotNull(pr.getState(), "every PullRequest must have a state");
            assertTrue(pr.getState().equals("open") || pr.getState().equals("closed"),
                "Lo stato deve essere 'open' o 'closed'");
          //this controls the parameter of the pull request, so it control that the object are passed correctly from JSON to java, the pull request must have a state and the state has to be or open or closed
          //the indicator then evalyate the percentage of closed pull request on the total number
        }
    }
}
