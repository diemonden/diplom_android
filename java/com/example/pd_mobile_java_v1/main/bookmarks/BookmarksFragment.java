package com.example.pd_mobile_java_v1.main.bookmarks;


import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.databinding.FragmentBookmarksBinding;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;

public class BookmarksFragment extends BindingFragment<BookmarksFragmentVM, FragmentBookmarksBinding> {

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment getInstance() {
        return new BookmarksFragment();
    }
    @Override
    protected BookmarksFragmentVM onCreateViewModel(FragmentBookmarksBinding fragmentBookmarksBinding) {
        return new BookmarksFragmentVM(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookmarks;
    }
}