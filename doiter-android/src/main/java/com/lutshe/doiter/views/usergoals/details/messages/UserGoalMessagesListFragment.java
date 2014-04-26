package com.lutshe.doiter.views.usergoals.details.messages;

import android.app.Fragment;
import android.widget.ListView;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.BackStackable;
import com.lutshe.doiter.views.UpdatableView;
import com.lutshe.doiter.views.common.OurFont;
import com.lutshe.doiter.views.usergoals.details.UserGoalDetailFragment_;
import com.lutshe.doiter.views.util.FragmentsSwitcher;
import org.androidannotations.annotations.*;

/**
 * Created by Arsen Adzhiametov on 12/15/13 in IntelliJ IDEA.
 */
@EFragment(R.layout.user_goal_messages_list_fragment)
public class UserGoalMessagesListFragment extends Fragment implements UpdatableView, BackStackable {

    @ViewById(R.id.user_goal_messages_list) ListView userGoalMessagesList;
    @ViewById(R.id.my_goals_btn_text) TextView myMessagesBtnText;

    @Bean MessagesListAdapter messagesListAdapter;
    @Bean FragmentsSwitcher fragmentsSwitcher;
    @Bean OurFont font;

    @FragmentArg Long goalId;

    @AfterViews
    public void bindData() {
        myMessagesBtnText.setTypeface(font.get());
        messagesListAdapter.initAdapter(goalId);
        userGoalMessagesList.setAdapter(messagesListAdapter);
    }

    @Override
    public void update() {
        messagesListAdapter.notifyDataSetChanged();
    }

    @Click(R.id.my_goals_btn)
    public void showMyGoals() {
        fragmentsSwitcher.show(UserGoalDetailFragment_.builder().goalId(goalId).build(), false);
    }
}
