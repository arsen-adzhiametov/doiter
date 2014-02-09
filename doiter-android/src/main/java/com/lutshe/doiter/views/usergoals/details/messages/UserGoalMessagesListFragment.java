package com.lutshe.doiter.views.usergoals.details.messages;

import android.app.Fragment;
import android.widget.ListView;
import org.androidannotations.annotations.*;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.UpdatableView;

/**
 * Created by Arsen Adzhiametov on 12/15/13 in IntelliJ IDEA.
 */
@EFragment(R.layout.user_goal_messages_list_fragment)
public class UserGoalMessagesListFragment extends Fragment implements UpdatableView, BackStackable {

    @ViewById(R.id.user_goal_messages_list)ListView userGoalMessagesList;

    @Bean MessagesListAdapter messagesListAdapter;

    @FragmentArg Long goalId;

    @AfterViews
    public void bindData() {
        messagesListAdapter.initAdapter(goalId);
        userGoalMessagesList.setAdapter(messagesListAdapter);
    }

    @Override
    public void update() {
        messagesListAdapter.notifyDataSetChanged();
    }
}
