package io.jenkins.plugins.jp_lock;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.Job;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class ProjectLockColumn extends ListViewColumn {

    @DataBoundConstructor
    public ProjectLockColumn() {
    }

    public boolean isLocked(Item item) {
        return getLockedBy(item) != null;
    }

    @CheckForNull
    public String getLockedBy(Item item) {
        if (item instanceof Job) {
            ProjectLockProperty prop = ((Job<?, ?>) item).getProperty(ProjectLockProperty.class);
            return prop != null ? prop.getLockedBy() : null;
        } else if (item instanceof AbstractFolder) {
            ProjectLockProperty.FolderProperty prop = ((AbstractFolder<?>) item).getProperties().get(ProjectLockProperty.FolderProperty.class);
            return prop != null ? prop.getLockedBy() : null;
        }
        return null;
    }

    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {
        @Override
        public String getDisplayName() {
            return "Lock Status";
        }

        @Override
        public boolean shownByDefault() {
            return true;
        }
    }
}
