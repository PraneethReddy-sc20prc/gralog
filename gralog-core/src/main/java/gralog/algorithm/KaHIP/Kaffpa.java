/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */
package gralog.algorithm.KaHIP;

import gralog.algorithm.*;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPAlgorithmParameters;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPPreferences;
import gralog.algorithm.KaHIP.KaHIPUtilities.KaHIPUtil;
import gralog.progresshandler.ProgressHandler;
import gralog.structure.Edge;
import gralog.structure.Highlights;
import gralog.structure.Structure;
import gralog.structure.Vertex;

import java.util.*;


/**
 *
 */
@AlgorithmDescription(
        name = "KAHIP - Kaffpa",
        text = "Apply multi-level graph partitioning algorithms and my other graph functionalities",
        url = ""
)
public class Kaffpa extends Algorithm {

    private static final List<String> requiredParameters = Arrays.asList("k", "seed", "preconfiguration", "imbalance", "time_limit", "enforce_balance", "balance_edges", "enable_mapping", "hierarchy_parameter_string", "distance_parameter_string", "online_distances");

    @Override
    public AlgorithmParameters getParameters(Structure s, Highlights highlights) {

        KaHIPPreferences p = new KaHIPPreferences();
        Map<String, List<String>> finalParameters = p.initializeParameters(this.getClass(), requiredParameters);

        return new KaHIPAlgorithmParameters(finalParameters.get("labels"), finalParameters.get("initialValues"), finalParameters.get("explanations"));
    }

    public Object run(Structure s, AlgorithmParameters params, Set<Object> selection, ProgressHandler onprogress) throws Exception {

        //Getting the parameters provided by the user
        KaHIPPreferences p = new KaHIPPreferences();
        List<String> commandParameters = p.getRequiredParameters(this.getClass(), requiredParameters, params);

        //Check if graph is empty
        if (s.getVertices().size() == 0)
            return ("The structure should not be empty.");

        Collection<Vertex> vertices = s.getVertices();
        Collection<Edge> edges = s.getEdges();

        KaHIPUtil kaHIPUtil = new KaHIPUtil();

        //Creating Metis file for the graph
        kaHIPUtil.generateMetisFormat(s, vertices.size() + " " + edges.size() + " 1\n", vertices);

        //Creating command
        final String workingDirectory = System.getProperty("user.dir") + "/KaHIP-master/deploy/";
        List<String> command = new ArrayList<>();
        command.add(workingDirectory);
        command.add("./kaffpa KaHIP_results/myGraph.graph ");
        command.add(String.join(" ", commandParameters));
        command.add(" --output_filename=KaHIP_results/output");
        System.out.println(String.join("", command));

        //Executing command
        final String commandOutput = kaHIPUtil.executeCommand(String.join("", command));

        //Generating parts
        if (commandOutput.split("\n\n").length > 1 && commandOutput.split("\n\n")[0].isEmpty())
            kaHIPUtil.generateParts(s, vertices, edges, "KaHIP_results/output", false);

        return commandOutput;
    }

}
