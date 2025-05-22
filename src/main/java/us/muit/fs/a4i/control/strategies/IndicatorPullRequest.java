import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;

public class PullRequestIndicatorStrategy implements IndicatorStrategy<Double> {  //this defines the indicator as a double
    
    private static final Logger log = Logger.getLogger(PullRequestIndicatorStrategy.class.getName()); //to show error messages during the execution
    private static final List<String> REQUIRED_METRICS = Arrays.asList("totalPullReq", "closedPullReq"); //this defines the mandatory metrics to evaluate the inidcator, so the total and closed pull requests

    @Override
    public ReportItem<Double> calcIndicator(List<ReportItem<Double>> metrics) throws NotAvailableMetricException { //method that evaluate the actual indicator, it obtains as an input the list of pullrequests 
        
        ReportItem<Double> indicatorReport = null;
        IndicatorState estado = IndicatorState.UNDEFINED;

        // Filter necessary metrics
        Optional<ReportItem<Double>> totalPullReq = metrics.stream()
                .filter(m -> REQUIRED_METRICS.get(0).equals(m.getName()))
                .findAny();

        Optional<ReportItem<Double>> closedPullReq = metrics.stream()
                .filter(m -> REQUIRED_METRICS.get(1).equals(m.getName()))
                .findAny();

        //here search for the mandatory metrics, and saves them in an Optional 

        // If both metrics are present it evaluates the inidcaotr, if one of them is missing goes to the else and gives an error
        if (totalPullReq.isPresent() && closedPullReq.isPresent()) {
            // Calculate the indicator
            Double pullRequestIndicator = 0.0;
            if (totalPullReq.get().getValue() > 0) { //this is done to remove the possibility of dividign by zero
                pullRequestIndicator = 100 * closedPullReq.get().getValue() / totalPullReq.get().getValue();
            }

            // Determine indicator state
            if (pullRequestIndicator > 75) {
                estado = IndicatorState.Correct;
            } else if (pullRequestIndicator > 50) {
                estado = IndicatorState.Precaution;
            } else {
                estado = IndicatorState.Critical;
            }

            // this create an object report that conatins the result of the class, so it has a name: PullRequestIndicator, the evaluated number, the metrics used for the calculation, the state
            try {
                indicatorReport = new ReportItem.ReportItemBuilder<Double>("pullRequestIndicator", pullRequestIndicator)
                        .metrics(Arrays.asList(totalPullReq.get(), closedPullReq.get()))
                        .indicator(estado)
                        .build();
        } else {
            log.info("Some required metrics are missing");
            throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
        }

        return indicatorReport; //return the indicator
    }

    @Override
    public List<String> requiredMetrics() {
        return REQUIRED_METRICS; //retrurn the metrics used to evaluate the indicator
    }
}
