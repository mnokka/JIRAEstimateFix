/* problem:  https://jira.atlassian.com/browse/JSW-9167
   Original solution: Alexandre Ouicher

This script to be script listener for project going to use scripted field solution
for getting subtask estimates to scrum board. 

Script runner (server) version 4.3.16 used

List of all events to register (custom script runner listener):

    Issue Created
    Issue Updated
    Work Logged On Issue
    Work Started On Issue
    Work Stopped On Issue
    Issue Worklog Updated
    Issue Worklog Deleted

*/

import com.atlassian.jira.issue.index.IssueIndexingService
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.util.ImportUtils
import org.apache.log4j.Category

import org.apache.log4j.Logger
import org.apache.log4j.Level



// set logging to Jira log
def log = Logger.getLogger("Estimate listener indexer") // change for customer system
log.setLevel(Level.INFO)
 

def issueManager = ComponentAccessor.getIssueManager()
def issueIndexingService = ComponentAccessor.getComponent(IssueIndexingService)
Issue issue = event.issue
boolean wasIndexing = ImportUtils.isIndexIssues();
ImportUtils.setIndexIssues(true);
log.info("Reindex issue ${issue.key} ${issue.id}")
issueIndexingService.reIndex(issueManager.getIssueObject(issue.id));
Issue parentIssue = issue.getParentObject();
if (parentIssue) {
	log.info("Reindex issue ${parentIssue.key} ${parentIssue.id}")
	issueIndexingService.reIndex(issueManager.getIssueObject(parentIssue.id));
}
ImportUtils.setIndexIssues(wasIndexing);





