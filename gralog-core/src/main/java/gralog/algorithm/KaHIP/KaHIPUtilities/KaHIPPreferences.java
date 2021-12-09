package gralog.algorithm.KaHIP.KaHIPUtilities;

import gralog.algorithm.AlgorithmParameters;
import gralog.preferences.Preferences;

import java.util.*;

import static gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPConstants.commandVariables;

public class KaHIPPreferences {

    // initialize all the parameters with labels, explanations and default values
    public Map<String, List<String>> initializeParameters(Class<?> forClass, List requiredParameters) {

        List<String> k = Arrays.asList("No of Partitions *", Preferences.getString(forClass, "k", "2"),
                "- Number of blocks to partition the graph into.");
        List<String> seed = Arrays.asList("Seed", Preferences.getString(forClass, "seed", "").toString(),
                "- Seed to use for the random number generator.");
        List<String> preconfiguration = Arrays.asList("Pre-configuration", Preferences.getString(forClass, "preconfiguration", "strong"),
                "- strong | eco | fast | fastsocial | ecosocial | strongsocial and only for ParHIP - ecosocial | fastsocial | ultrafastsocial | ecomesh | fastmesh | ultrafastmesh");
        List<String> imbalance = Arrays.asList("Imbalance", Preferences.getString(forClass, "imbalance", ""),
                "- Desired balance. Default: 3 (%).");
        List<String> time_limit = Arrays.asList("Time limit", Preferences.getString(forClass, "time_limit", "0"),
                "- Time limit in seconds. The default value is set to 0");
        List<String> enforce_balance = Arrays.asList("Enforce Balance", Preferences.getBoolean(forClass, "enforce_balance", false).toString(),
                "- Use this option only on graphs without vertex weights.");
        List<String> balance_edges = Arrays.asList("Balance Edges", Preferences.getBoolean(forClass, "balance_edges", false).toString(),
                "- Use this option to balance the edges among the blocks as well as the nodes.");
        List<String> input_partition = Arrays.asList("Input Partition", Preferences.getString(forClass, "input_partition", ""), "");
        List<String> enable_mapping = Arrays.asList("Enable Mapping", Preferences.getBoolean(forClass, "enable_mapping", false).toString(),
                "- Enable mapping algorithms to map quotient graph onto processor graph defined by hierarchy and distance options.");
        List<String> hierarchy_parameter_string = Arrays.asList("Hierarchy Parameter", Preferences.getString(forClass, "hierarchy_parameter_string", ""),
                "- Specify as 4:8:8 for 4 cores per PE, 8 PEs per rack, ... and so forth.");
        List<String> distance_parameter_string = Arrays.asList("Distance Parameter", Preferences.getString(forClass, "distance_parameter_string", ""),
                "- Specify as 1:10:100 if cores on the same chip have distance 1, PEs in the same rack have distance 10, ... and so forth.");
        List<String> online_distances = Arrays.asList("Online Distances", Preferences.getBoolean(forClass, "online_distances", false).toString(),
                "- Do not store processor distances in a matrix, but do recomputation.");
        List<String> output_filename = Arrays.asList("Output filename", Preferences.getString(forClass, "output_filename", "output"),
                "- Specify the output Metis filename.");

        //for KaffpaE
        List<String> processes = Arrays.asList("Number of processes to use *", Preferences.getString(forClass, "noOfProcesses", "2"), "");
        List<String> mh_enable_quickstart = Arrays.asList("mh_enable_quickstart", Preferences.getBoolean(forClass, "mh_enable_quickstart", false).toString(),
                "- Enables the quickstart option.");
        List<String> mh_optimize_communication_volume = Arrays.asList("mh_optimize_communication_volume", Preferences.getBoolean(forClass, "mh_optimize_communication_volume", false).toString(),
                "- Modifies the fitness function of the evolutionary algorithm so that communication volume is optimized.");
        List<String> mh_enable_kabapE = Arrays.asList("mh_enable_kabapE", Preferences.getBoolean(forClass, "mh_enable_kabapE", false).toString(),
                "- Enables the combine operator of KaBaPE.");
        List<String> mh_enable_tabu_search = Arrays.asList("mh_enable_tabu_search", Preferences.getBoolean(forClass, "mh_enable_tabu_search", false).toString(),
                "- Enables our version of combine operation by block matching.");
        List<String> kabaE_internal_bal = Arrays.asList("kabaE_internal_bal", Preferences.getString(forClass, "kabaE_internal_bal", ""),
                "- The internal balance parameter for KaBaPE (Default: 0.01) (1 %)");

        List<String> save_partition = Arrays.asList("Save partition", Preferences.getBoolean(forClass, "save_partition", false).toString(),
                "- Enable this tag if you want to store the partition to disk.");
        List<String> save_partition_binary = Arrays.asList("Save partition binary", Preferences.getBoolean(forClass, "save_partition_binary", false).toString(),
                "- Enable this tag if you want to store the partition to disk in a binary format.");
        List<String> evaluate = Arrays.asList("Evaluate", Preferences.getBoolean(forClass, "evaluate", false).toString(),
                "- Enable this tag the partition to be evaluated.");

        //for node ordering
        List<String> reduction_order = Arrays.asList("Reduction order", Preferences.getString(forClass, "reduction_order", ""),
                "- 0: simplical node reduction, 1: indis- tinguishable_nodes, 2: twins, 3: path_compression, 4: degree_2_nodes, 5: trian- gle_contraction");

        Map<String, List<String>> allParameters = new HashMap<>();
        allParameters.put("k", k);
        allParameters.put("seed", seed);
        allParameters.put("preconfiguration", preconfiguration);
        allParameters.put("imbalance", imbalance);
        allParameters.put("time_limit", time_limit);
        allParameters.put("enforce_balance", enforce_balance);
        allParameters.put("balance_edges", balance_edges);
        allParameters.put("input_partition", input_partition);
        allParameters.put("enable_mapping", enable_mapping);
        allParameters.put("hierarchy_parameter_string", hierarchy_parameter_string);
        allParameters.put("distance_parameter_string", distance_parameter_string);
        allParameters.put("online_distances", online_distances);
        allParameters.put("output_filename", output_filename);
        allParameters.put("processes", processes);
        allParameters.put("mh_enable_quickstart", mh_enable_quickstart);
        allParameters.put("mh_optimize_communication_volume", mh_optimize_communication_volume);
        allParameters.put("mh_enable_tabu_search", mh_enable_tabu_search);
        allParameters.put("kabaE_internal_bal", kabaE_internal_bal);
        allParameters.put("mh_enable_kabapE", mh_enable_kabapE);
        allParameters.put("save_partition", save_partition);
        allParameters.put("save_partition_binary", save_partition_binary);
        allParameters.put("evaluate", evaluate);
        allParameters.put("reduction_order", reduction_order);

        return generateRequiredParameterProperties(requiredParameters, allParameters);

    }

    //Return only the required parameters from all the parameters of KaHIP algorithms
    public Map<String, List<String>> generateRequiredParameterProperties(List requiredParameters, Map<String, List<String>> allParameters) {
        List<String> labels = new ArrayList<>();
        List<String> initialValues = new ArrayList<>();
        List<String> explanations = new ArrayList<>();

        List<String> temp;
        for (Object requiredParameter : requiredParameters) {
            temp = allParameters.get(requiredParameter);
            labels.add(temp.get(0));
            initialValues.add(temp.get(1));
            explanations.add(temp.get(2));
        }

        Map<String, List<String>> finalParameters = new HashMap<String, List<String>>();
        finalParameters.put("labels", labels);
        finalParameters.put("initialValues", initialValues);
        finalParameters.put("explanations", explanations);

        return finalParameters;
    }

    //Return the list of parameters with strings of terminal command parameters
    public List<String> getRequiredParameters(Class<?> forClass, List<String> requiredParameters, AlgorithmParameters params) {

        List<String> commandList = new ArrayList<>();
        String var = "";

        for (int i = 0; i < requiredParameters.size(); i++) {
            var = ((KaHIPAlgorithmParameters) params).parameters.get(i);
            if (!Objects.equals(var, "") && !Objects.equals(var, "false")) {
                if (Objects.equals(var, "true"))
                    commandList.add(commandVariables.get(requiredParameters.get(i)));
                else
                    commandList.add(commandVariables.get(requiredParameters.get(i)) + var);
                Preferences.setString(forClass, requiredParameters.get(i), var);
            }
        }

        return commandList;
    }
}


