/* This file is part of Gralog, Copyright (c) 2016-2018 LaS group, TU Berlin.
 * License: https://www.gnu.org/licenses/gpl.html GPL version 3 or later. */
package gralog.algorithm.KaHIP;

import gralog.algorithm.Algorithm;
import gralog.algorithm.AlgorithmDescription;
import gralog.algorithm.AlgorithmParameters;
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
        name = "KAHIP - Distributed Edge Partitioning",
        text = "This is the multilevel graph partitioning program.",
        url = "https://kahip.github.io"
)
public class DistributedEdgePartitioning extends Algorithm {

    private static final List<String> requiredParameters = Arrays.asList("processes","k","seed", "preconfiguration", "imbalance");

    @Override
    public AlgorithmParameters getParameters(Structure s, Highlights highlights) {

        KaHIPPreferences p = new KaHIPPreferences();
        Map<String, List<String>> finalParameters = p.initializeParameters(this.getClass(), requiredParameters);

        return new KaHIPAlgorithmParameters(finalParameters.get("labels"), finalParameters.get("initialValues"), finalParameters.get("explanations"));
    }

    public Object run(Structure s, AlgorithmParameters params, Set<Object> selection, ProgressHandler onprogress) throws Exception {

        KaHIPPreferences p = new KaHIPPreferences();

        List<String> commandParameters = p.getRequiredParameters(this.getClass(),requiredParameters, params);
        List<String> subCommand = commandParameters.subList(2, commandParameters.size());

        if (s.getVertices().size() == 0)
            return ("The structure should not be empty.");

        Collection<Vertex> vertices = s.getVertices();
        Collection<Edge> edges = s.getEdges();

        KaHIPUtil kaHIPUtil = new KaHIPUtil();

        //Creating Metis file for the graph
        List<Integer> edgeIDs = kaHIPUtil.generateMetisFormat(s, vertices.size() + " " + edges.size() + " 1\n", vertices);

        //Creating command
        final String workingDirectory = System.getProperty("user.dir") + "/KaHIP-master/deploy/";
        List<String> command = new ArrayList<>();

        //Executing command - Graph2Binary
        kaHIPUtil.executeCommand(workingDirectory+ "./graph2binary KaHIP_results/myGraph.graph KaHIP_results/tempGraph.bgf");

        //Creating command - Distributed Edge Partitioning
        String pValue = Objects.equals(commandParameters.get(0), "") ? "1" : commandParameters.get(0);
        command.add("mpirun -n " + pValue + " " + workingDirectory + "./distributed_edge_partitioning ");
        command.add(commandParameters.get(1));
        command.add(" KaHIP_results/tempGraph.bgf");
        command.add(" --output_filename=KaHIP_results/output");
        command.add(" --save_partition ");
        command.add(String.join(" ", subCommand));

        final String commandOutput =  kaHIPUtil.executeCommand(String.join("", command));

        if(commandOutput.split("\n\n").length >1 && commandOutput.split("\n\n")[0].isEmpty())
            kaHIPUtil.generateEdgeParts(s, edgeIDs);

        return commandOutput;
    }
}
