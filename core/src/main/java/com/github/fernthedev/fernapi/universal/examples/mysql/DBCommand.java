package com.github.fernthedev.fernapi.universal.examples.mysql;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.ColumnData;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;

import java.util.concurrent.ExecutionException;

@CommandAlias("db")
public class DBCommand extends BaseCommand {


    private static final DatabaseTest db = new DatabaseTest(null,null,null,null,null);

    private static final String dbValues = "start|insert|get|remove";


    @Subcommand(dbValues)
    @CommandCompletion(dbValues)
    @Default
    @Description("Test database system code")
    public void onDefault(CommandIssuer sender, @Values(dbValues) String arg) {
        try {
            switch (arg.toLowerCase()) {
                case "start":
                    db.test();
                    break;
                case "insert":
                    RowDataTest rowData = new RowDataTest("row1", "value1test");

                    db.insertIntoTable(db.getTableInfo(), rowData);
                    break;
                case "get":
                    for (RowData rowData1 : db.getTable(db.getTableInfo().getTableName(), RowDataTest.class)
                            .get().getRowDataListCopy().values()) {
                        Universal.getLogger().info(rowData1.getDataCopy().toString());

                        for (ColumnData columnData : rowData1.getDataCopy().values()) {
                            Universal.getLogger().info(columnData.getColumnName() + ":" + columnData.getValue() + " (" + columnData.getType() + "|" + columnData.getLength() + ")\n");
                        }
                    }
                    break;
                case "remove":
                    db.removeTable(db.getTableInfo());
                    break;
                default:
                    showSyntax(sender, getDefaultRegisteredCommand());
            }
        } catch (DatabaseException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
