package com.github.fernthedev.fernapi.universal.examples.mysql;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.ColumnData;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.data.database.RowDataTemplate;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;

import java.util.Arrays;

@CommandAlias("db")
public class DBCommand extends BaseCommand {


    private static DatabaseTest db = new DatabaseTest(null,null,null,null,null);;

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
                    RowData rowData = new RowData(new ColumnData("row1", "value1test"), new ColumnData("row2", "value2test"));

                    db.insertIntoTable(db.getTableInfo(), rowData);
                    break;
                case "get":
                    for (RowData rowData1 : db.getTable(db.getTableInfo().getTableName(),
                            new RowDataTemplate(new ColumnData("row1", "value1test"),
                                    new ColumnData("row2", "value2test"))).getRowDataList()) {
                        Universal.getLogger().info(Arrays.toString(rowData1.getColumnDataList().toArray()));

                        for (ColumnData columnData : rowData1.getColumnDataList()) {
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
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }
}
