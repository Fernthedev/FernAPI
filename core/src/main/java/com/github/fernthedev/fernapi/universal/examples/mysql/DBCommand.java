package com.github.fernthedev.fernapi.universal.examples.mysql;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.database.ColumnData;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;
import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@CommandAlias("db")
public class DBCommand extends BaseCommand {


    private static DatabaseTest db;

    private static final String dbValues = "start|insert|get|remove";


    @CommandCompletion(dbValues)
    @Default
    @Description("Test database system code")
    public void onDefault(FernCommandIssuer sender, @Values(dbValues) String arg) {
        if (db == null) db = new DatabaseTest();

        try {
            switch (arg.toLowerCase()) {
                case "start":
                    db.test();
                    break;
                case "insert":
                    RowDataTest rowData = new RowDataTest("row1", "value1test");

                    db.insertIntoTable(db.getTableInfo(), rowData).thenRun(() -> sender.sendMessage(Component.text("Inserted row").color(NamedTextColor.GREEN)));
                    break;
                case "get":
                    db.getTableInfo().loadFromDB(db).thenRun(() -> {
                        for (RowData rowData1 : db.getTableInfo().getRowDataListCopy().values()) {
                            Universal.getLogger().info(ChatColor.RED + rowData1.getDataCopy().toString());
                            Universal.getLogger().info(ChatColor.AQUA + new Gson().toJson(rowData1));

                            for (ColumnData columnData : rowData1.getDataCopy().values()) {
                                Universal.getLogger().info(ChatColor.YELLOW + columnData.getColumnName() + ":" + columnData.getValue() + " (" + columnData.getType() + "|" + columnData.getLength() + ")\n");
                            }
                        }
                    });
 
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
