/*  https://jira.atlassian.com/browse/JSW-9167
 Original solution: Alexandre Ouicher
 

    install or user ScriptRunner pluggin
    Create 2 custom fields
        Compound Estimate (type: Scripted field)
        Total Remaining Estimate (type: numeric field)
    Go to scripted field "Compound Estimate" and put the code on below
    Go to scripts listeners (script Runner) and create a custom script and put the second script
    Go to your board configuration and change the "estimation" to use your new field "Total Remaining Estimate"


Scrum board estimating must be changed to use new custom field "Total Remaining Estimate"
Both custom fields to be added to the project screens
Script runner (server) V4.3.16 was used ok (earlier version failed with this code)

 */


import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.util.ImportUtils;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.core.util.DateUtils;
import com.atlassian.jira.issue.index.IssueIndexingService;
import com.atlassian.jira.issue.managers.DefaultIssueManager;
import java.lang.Long;
import org.apache.log4j.Category

import org.apache.log4j.Logger
import org.apache.log4j.Level



// set logging to Jira log
def log = Logger.getLogger("Estimate fix script") // change for customer system
log.setLevel(Level.INFO)
 


def cfManager = ComponentAccessor.getCustomFieldManager();
def compoundsum = 0;
compoundsum += getRemainingEstimate(issue);

SubTaskManager subTaskManager = ComponentAccessor.getSubTaskManager();
Collection subTasks = issue.getSubTaskObjects();

log.info(issue);


if (subTaskManager.subTasksEnabled && !subTasks.empty) {
    subTasks.each {
               compoundsum += getRemainingEstimate(it);
    } 
}


def compound = cfManager.getCustomFieldObjectByName("Total Remaining Estimate");
compound.updateValue(null, issue, new ModifiedValue(issue.getCustomFieldValue(compound), compoundsum), new DefaultIssueChangeHolder());

return  DateUtils.getDurationString(compoundsum.toLong(),8,5);


//returns the remaining estimate (in hours) for an issue and returns 0 if there is none.
double getRemainingEstimate(Issue issue) {

double d = 0;
def rfValue = issue.getEstimate();
if(rfValue > 0){
	def rfDouble = (double) rfValue;
	d = rfDouble / 3600;
return d;
}else{
	return (long) 0;
}
}
