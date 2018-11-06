package com.enjoy02.enjoyfragmentdemo02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.enjoy02.enjoyfragmentdemo02.activity.Bug1Activity;
import com.enjoy02.enjoyfragmentdemo02.activity.Bug2Activity;
import com.enjoy02.enjoyfragmentdemo02.activity.Bug3Activity;
import com.enjoy02.enjoyfragmentdemo02.activity.Bug4Activity;
import com.enjoy02.enjoyfragmentdemo02.activity.Bug5Activity;
import com.enjoy02.enjoyfragmentdemo02.activity.ViewpagerActivity;


public class MainFragment extends ListFragment {


    public static Fragment newIntance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    ArrayAdapter<String> arrayAdapter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "getActivity==null",
                "Can not perform this action after onSaveInstanceState",
                "Fragment重叠异常",
                "嵌套的fragment不能在onActivityResult()中接收到返回值",
                "未必靠谱的出栈方法remove()",
                "mAvailIndeices的BUG",
                "popBackStack的坑",
                "pop多个Fragment时转场动画 带来的问题",
                "进入新的Fragment并立刻关闭当前Fragment 时的一些问题",
                "Fragment+viewpager"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        String item = arrayAdapter.getItem(position);
        Toast.makeText(getActivity(), item, Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                Intent intent0 = new Intent(getActivity(), Bug1Activity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(getActivity(), Bug2Activity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), Bug3Activity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(getActivity(), Bug4Activity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(getActivity(), Bug5Activity.class);
                startActivity(intent4);
                break;
            case 9:
                Intent intent9 = new Intent(getActivity(), ViewpagerActivity.class);
                startActivity(intent9);
                break;
            default:
                break;
        }
    }


}
