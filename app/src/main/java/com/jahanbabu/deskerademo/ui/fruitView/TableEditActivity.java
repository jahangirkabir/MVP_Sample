package com.jahanbabu.deskerademo.ui.fruitView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jahanbabu.deskerademo.R;
import com.jahanbabu.deskerademo.data.Fruit;
import java.util.List;

public class TableEditActivity extends AppCompatActivity implements IAddCheckBoxCheckdListener,
    View.OnClickListener {

  private static final String TAG = TableEditActivity.class.getSimpleName();

  private RecyclerView mrvTableItems;
  private TextView mtvPrimary, mtvTitle;
  private Button mBtnDelete;

//  private EditableTableAdapter mEditTableAdapter;
  private List<Fruit> mTableItemList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_table_edit);
    initView();
    setupToolabar();
    setLayoutManager();
    addListener();
    loadData();
  }

  private void initView() {
    mrvTableItems = findViewById(R.id.rv_list);
    mtvPrimary = findViewById(R.id.primaryTextView);
    mtvTitle = findViewById(R.id.toolbar_title);
    mBtnDelete = findViewById(R.id.btnDelete);
  }

  private void setupToolabar() {
    mtvPrimary.setVisibility(View.VISIBLE);
    mtvPrimary.setText(R.string.done);
    mtvTitle.setText(R.string.delete_items);
  }


  private void setLayoutManager() {
    mrvTableItems.setLayoutManager(new LinearLayoutManager(this));
    DividerItemDecoration decoration =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//    mEditTableAdapter = new EditableTableAdapter(this);
//    mrvTableItems.addItemDecoration(decoration);
//    mrvTableItems.setAdapter(mEditTableAdapter);
  }

  private void loadData() {
//    mTableItemList = DatabaseManager.getDatabase().getTabletTabItemDao().getAll();
//    Collections.sort(mTableItemList, new Comparator<TabletTabItem>() {
//      @Override
//      public int compare(TabletTabItem item1, TabletTabItem item2) {
//        return item1.getName().compareToIgnoreCase(item2.getName());
//      }
//    });
//    mEditTableAdapter.setTableDataList(mTableItemList);
  }

  private void addListener() {
    mtvPrimary.setOnClickListener(this);
    mBtnDelete.setOnClickListener(this);
  }

  @Override
  public void checkBoxChanged(boolean isChecked, int count) {
    mtvTitle.setText(getString(R.string.selected, count));
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.primaryTextView:
        finish();
        break;

      case R.id.btnDelete:
        deleteButtonClick();
        break;
    }
  }

  private void deleteButtonClick() {
//    List<TabletTabItem> deleteList = mEditTableAdapter.getDeleteItemList();
//    if (null != deleteList && !deleteList.isEmpty()) {
//      DatabaseManager.getDatabase().getTabletTabItemDao().deleteMultiples(deleteList);
//      BusProvider.getInstance().post(new TableItemModifiedEvent());
//      mTableItemList.removeAll(deleteList);
//      mEditTableAdapter.setTableDataList(mTableItemList);
//      mEditTableAdapter.getDeleteItemList().clear();
//      mtvTitle.setText(R.string.delete_items);
//    }
  }
}