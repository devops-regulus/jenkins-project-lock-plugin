package io.jenkins.plugins.jp_lock;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.AbstractItem;
import hudson.model.Action;
import hudson.model.Job;
import hudson.model.User;
import jenkins.model.TransientActionFactory;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.interceptor.RequirePOST;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class LockAction implements Action {

    private final AbstractItem item;

    public LockAction(AbstractItem item) {
        this.item = item;
    }

    @Override
    public String getIconFileName() {
        return isLocked() ? "symbol-lock-closed-outline plugin-ionicons-api" : "symbol-lock-open-outline plugin-ionicons-api";
    }

    @Override
    public String getDisplayName() {
        return isLocked() ? "Unlock project" : "Occupy the project";
    }

    @Override
    public String getUrlName() {
        return "project-lock";
    }

    public AbstractItem getItem() {
        return item;
    }

    public boolean isLocked() {
        return getLockedBy() != null;
    }

    @CheckForNull
    public String getLockedBy() {
        if (item instanceof Job) {
            ProjectLockProperty prop = ((Job<?, ?>) item).getProperty(ProjectLockProperty.class);
            return prop != null ? prop.getLockedBy() : null;
        } else if (item instanceof AbstractFolder) {
            ProjectLockProperty.FolderProperty prop = ((AbstractFolder<?>) item).getProperties().get(ProjectLockProperty.FolderProperty.class);
            return prop != null ? prop.getLockedBy() : null;
        }
        return null;
    }

    @RequirePOST
    public HttpResponse doLock() throws IOException {
        User currentUser = User.current();
        if (currentUser == null) {
            return HttpResponses.forbidden();
        }

        if (item instanceof Job) {
            ((Job<?, ?>) item).addProperty(new ProjectLockProperty(currentUser.getId(), System.currentTimeMillis()));
        } else if (item instanceof AbstractFolder) {
            ((AbstractFolder<?>) item).addProperty(new ProjectLockProperty.FolderProperty(currentUser.getId(), System.currentTimeMillis()));
        }
        item.save();
        // Redirect back to the project page (parent of this action)
        return HttpResponses.redirectTo("..");
    }

    @RequirePOST
    public HttpResponse doUnlock() throws IOException {
        if (item instanceof Job) {
            ((Job<?, ?>) item).removeProperty(ProjectLockProperty.class);
        } else if (item instanceof AbstractFolder) {
            ((AbstractFolder<?>) item).getProperties().remove(ProjectLockProperty.FolderProperty.class);
        }
        item.save();
        // Redirect back to the project page (parent of this action)
        return HttpResponses.redirectTo("..");
    }

    /**
     * Handles the side panel link click (GET).
     */
    public HttpResponse doIndex() throws IOException {
        User currentUser = User.current();
        if (currentUser == null) {
            return HttpResponses.forbidden();
        }

        if (isLocked()) {
            // Only unlock if we want to toggle or handle it simply
            if (item instanceof Job) {
                ((Job<?, ?>) item).removeProperty(ProjectLockProperty.class);
            } else if (item instanceof AbstractFolder) {
                ((AbstractFolder<?>) item).getProperties().remove(ProjectLockProperty.FolderProperty.class);
            }
        } else {
            if (item instanceof Job) {
                ((Job<?, ?>) item).addProperty(new ProjectLockProperty(currentUser.getId(), System.currentTimeMillis()));
            } else if (item instanceof AbstractFolder) {
                ((AbstractFolder<?>) item).addProperty(new ProjectLockProperty.FolderProperty(currentUser.getId(), System.currentTimeMillis()));
            }
        }
        item.save();
        
        // CRITICAL: Redirect to the project page, NOT back to this action URL
        return HttpResponses.redirectTo("..");
    }

    @Extension
    public static class Factory extends TransientActionFactory<AbstractItem> {
        @Override
        public Class<AbstractItem> type() {
            return AbstractItem.class;
        }

        @Override
        public Collection<? extends Action> createFor(AbstractItem item) {
            return Collections.singleton(new LockAction(item));
        }
    }
}
