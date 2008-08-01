package org.flexpay.common.process;

@Deprecated
/**
 * Use org.flexpay.common.process.job.Job class for default transition names
 */
public abstract class ActionHandler implements org.jbpm.graph.def.ActionHandler {

    public static final String RESULT_NEXT = "next";
    public static final String RESULT_ERROR = "error";

    
}
