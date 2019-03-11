package com.github.fernthedev.fernapi.universal.examples;

import com.github.fernthedev.fernapi.server.bungee.FernCommand;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.data.database.RowObject;
import net.md_5.bungee.api.CommandSender;

import java.util.Arrays;

public class DBCommand extends FernCommand {
    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public DBCommand(String name) {
        super(name);
        onConst();
    }

    private void onConst() {
        db = new DatabaseTest(null,null,null,null,null);
    }

    private static DatabaseTest db;

    /**
     * Construct a new command.
     *
     * @param name       primary name of this command
     * @param permission the permission node required to execute this command,
     *                   null or empty string allows it to be executed by everyone
     * @param aliases    aliases which map back to this command
     */
    public DBCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        onConst();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sendMessage(sender,"&cNo args?");
        }else{
            switch (args[0].toLowerCase()) {

                case "start":
                    db.test();
                    break;
                case "insert":
                    RowData rowData = new RowData(new RowObject("row1", "value1test"));

                    rowData.addData(new RowObject("row2", "value2test"));
                    db.insertIntoTable(db.getTableInfo(), rowData);
                    break;
                case "get":
                    for (RowData rowData1 : db.getTable(db.getTableInfo().getTableName()).getRowDataList()) {
                        logger().info(Arrays.toString(rowData1.getObjects().toArray()));

                        for (RowObject rowObject : rowData1.getObjects()) {
                            logger().info(rowObject.getRow() + ":" + rowObject.getValue() + " (" + rowObject.getType() + "|" + rowObject.getLength() + ")\n");
                        }
                    }
                    break;
                case "remove":
                    db.removeTable(db.getTableInfo());
                    break;
            }
        }
    }
}