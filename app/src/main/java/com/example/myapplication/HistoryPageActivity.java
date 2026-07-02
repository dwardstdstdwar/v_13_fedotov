package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryPageActivity extends AppCompatActivity {

    private ImageButton btnBack, btnHistoryAction;
    private RadioGroup rgSortOptions;
    private RadioButton rbNewFirst, rbOldFirst, rbPostponed;
    private Button btnReset;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        // Инициализируем БД
        dbHelper = new DatabaseHelper(this);

        // Находим все View компоненты
        btnBack = findViewById(R.id.btnBack);
        btnHistoryAction = findViewById(R.id.btnHistoryAction);
        rgSortOptions = findViewById(R.id.rgSortOptions);
        rbNewFirst = findViewById(R.id.rbNewFirst);
        rbOldFirst = findViewById(R.id.rbOldFirst);
        rbPostponed = findViewById(R.id.rbPostponed);
        btnReset = findViewById(R.id.btnReset);

        // Загружаем сохраненный статус из Базы Данных SQLite
        int savedOption = dbHelper.getSortOption();
        if (savedOption == 1) {
            rbOldFirst.setChecked(true);
        } else if (savedOption == 2) {
            rbPostponed.setChecked(true);
        } else {
            rbNewFirst.setChecked(true);
        }

        // Кнопка "Назад" — закрывает этот экран
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Кнопка действия в тулбаре
        btnHistoryAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HistoryPageActivity.this, "Действие истории выполнено", Toast.LENGTH_SHORT).show();
            }
        });

        // Слушатель изменения выбора в RadioGroup для сохранения в БД
        rgSortOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNewFirst) {
                    dbHelper.saveSortOption(0);
                    Toast.makeText(HistoryPageActivity.this, "Сортировка: Сначала новые", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rbOldFirst) {
                    dbHelper.saveSortOption(1);
                    Toast.makeText(HistoryPageActivity.this, "Сортировка: Сначала старые", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rbPostponed) {
                    dbHelper.saveSortOption(2);
                    Toast.makeText(HistoryPageActivity.this, "Сортировка: Перенесенные", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Большая скруглённая кнопка "Сбросить настройки"
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.saveSortOption(0);
                rbNewFirst.setChecked(true);
                Toast.makeText(HistoryPageActivity.this, "Настройки сброшены по умолчанию", Toast.LENGTH_SHORT).show();
            }
        });
    }
}