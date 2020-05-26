package iss.work.mantis.model;

import com.google.common.collect.ForwardingSet;
import iss.work.mantis.appmanager.UserHelper;

import java.util.HashSet;
import java.util.Set;

public class Users extends ForwardingSet<UserData> {
    private Set<UserData> delegate;

    public Users(Users users) {
        this.delegate = new HashSet<UserData>(users.delegate);
    }

    public Users() {
        this.delegate = new HashSet<UserData>();
    }

    @Override
    protected Set<UserData> delegate() {
        return delegate;
    }

    public UserData getNot(String s) {
        return delegate.stream().filter((u) -> !u.getUsername().equals(s)).findFirst().get();
    }
}
