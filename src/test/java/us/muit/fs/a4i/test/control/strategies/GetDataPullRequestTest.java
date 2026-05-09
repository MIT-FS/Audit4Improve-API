package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.strategies.GetDataPullRequest;

// Correccion del test por Álvaro Pérez

public class GetDataPullRequestTest {

    @Test
    public void testGetTotalPullRequests_basic() throws Exception {


        GetDataPullRequest enquirer = new GetDataPullRequest();

        //Uso de variables de entorno para configurar el propietario y el repositorio de GitHub
        String owner = System.getenv("GITHUB_OWNER");
        String repo = System.getenv("GITHUB_REPO");

        assumeTrue(owner != null && !owner.isBlank(), "GITHUB_OWNER is not configured");
        assumeTrue(repo != null && !repo.isBlank(), "GITHUB_REPO is not configured");
        
        int pullRequests = enquirer.getTotalPullRequests(owner, repo);

        assertTrue(
                pullRequests >= 0,
                "the number of pull requests cannot be negative"
        );
    }
}