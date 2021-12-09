package gralog.algorithm.KaHIP.KaHIPUtilities;

import java.util.Map;

import static java.util.Map.entry;

public class KaHIPConstants {

    private KaHIPConstants() {
        // restrict instantiation
    }

    public static final Map<String, String> commandVariables = Map.ofEntries(
            entry("k", "--k="),
            entry("seed", "--seed="),
            entry("preconfiguration", "--preconfiguration="),
            entry("imbalance", "--imbalance="),
            entry("time_limit", "--time_limit="),
            entry("enforce_balance", "--enforce_balance"),
            entry("balance_edges", "--balance_edges"),
            entry("input_partition", "--input_partition="),
            entry("enable_mapping", "--enable_mapping"),
            entry("hierarchy_parameter_string", "--hierarchy_parameter_string="),
            entry("distance_parameter_string", "--distance_parameter_string="),
            entry("online_distances", "--online_distances"),
            entry("output_filename", "--output_filename="),
            entry("processes", ""),
            entry("mh_enable_quickstart", "--mh_enable_quickstart="),
            entry("mh_optimize_communication_volume", "--mh_optimize_communication_volume="),
            entry("mh_enable_tabu_search", "--mh_enable_tabu_search"),
            entry("mh_enable_kabapE", "--mh_enable_kabapE"),
            entry("kabaE_internal_bal", "--kabaE_internal_bal="),
            entry("save_partition", "--save_partition"),
            entry("save_partition_binary", "--save_partition_binary"),
            entry("evaluate", "-evaluate"),
            entry("reduction_order", "--reduction_order=")
            );
}
