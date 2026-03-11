package io.jenkins.plugins.jp_lock;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class ProjectLockProperty extends JobProperty<Job<?, ?>> {

    private String lockedBy;
    private long lockTime;

    @DataBoundConstructor
    public ProjectLockProperty(String lockedBy, long lockTime) {
        this.lockedBy = lockedBy;
        this.lockTime = lockTime;
    }

    @CheckForNull
    public String getLockedBy() {
        return lockedBy;
    }

    public long getLockTime() {
        return lockTime;
    }

    public boolean isLocked() {
        return lockedBy != null && !lockedBy.isEmpty();
    }

    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "Project Lock Status";
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return true;
        }
    }

    /**
     * Folder property for Multibranch projects/Folders
     */
    public static class FolderProperty extends AbstractFolderProperty<AbstractFolder<?>> {
        private String lockedBy;
        private long lockTime;

        @DataBoundConstructor
        public FolderProperty(String lockedBy, long lockTime) {
            this.lockedBy = lockedBy;
            this.lockTime = lockTime;
        }

        @CheckForNull
        public String getLockedBy() {
            return lockedBy;
        }

        public boolean isLocked() {
            return lockedBy != null && !lockedBy.isEmpty();
        }

        @Extension
        public static class DescriptorImpl extends AbstractFolderPropertyDescriptor {
            @Override
            public String getDisplayName() {
                return "Folder Lock Status";
            }
        }
    }
}
