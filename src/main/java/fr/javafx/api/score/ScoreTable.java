package fr.javafx.api.score;

import java.util.concurrent.Callable;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import fr.java.api.score.Score;

public class ScoreTable extends TableView<Score> {
	public static final double EntryHeight = 27d;
	public static record       Entry(String name, long score) {}

    public ScoreTable() {
    	this(10);
    }
    @SuppressWarnings("unchecked")
	public ScoreTable(int _nbEntry) {
    	super();

    	TableColumn<Score, String> one, two, three;

        getColumns().add( one   = createRankColumn() );
        getColumns().add( two   = createNameColumn() );
        getColumns().add( three = createScoreColumn() );

        Platform.runLater(() -> adjustSize(_nbEntry, two, one, three));
    }

    private TableColumn<Score, String> createRankColumn() {
        TableColumn<Score, String> rankColumn = new TableColumn<Score, String>("RANK");

        rankColumn.setPrefWidth(50);
        rankColumn.setStyle( "-fx-alignment: CENTER;");
        rankColumn.setCellFactory(new Callback<TableColumn<Score, String>, TableCell<Score, String>>() {
            @Override
            public TableCell<Score, String> call(TableColumn<Score, String> param) {
                return new TableCell<Score, String>() {

                    @Override
                    public void updateItem(String _null, boolean empty) {
                        super.updateItem(_null, empty);
                        if (!isEmpty()) {
                            int   selectedIndex  = getTableRow().getIndex();
//                          Score selectedRecord = (Score) getItems().get(selectedIndex);

                            Label 
                            rank = new Label("#" + String.valueOf(selectedIndex+1));

                            setGraphic(rank);
                        }
                    }

                };
            }
        });

        return rankColumn;
    }
    private TableColumn<Score, String> createNameColumn() {
        TableColumn<Score, String> nameColumn = new TableColumn<Score, String>("NAME");

        nameColumn.setPrefWidth(369);
        nameColumn.setStyle( "-fx-alignment: CENTER;");
        nameColumn.setCellFactory(new Callback<TableColumn<Score, String>, TableCell<Score, String>>() {
            @Override
            public TableCell<Score, String> call(TableColumn<Score, String> param) {
                final TableCell<Score, String> cell = new TableCell<Score, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            int   selectedIndex  = getTableRow().getIndex();
                            Score selectedRecord = (Score) getItems().get(selectedIndex);

                            Label 
                            name = new Label(selectedRecord.getUserName());

                            setGraphic(name);
                        }
                    }
                };
                return cell;
            }
        });

        return nameColumn;
    }
    private TableColumn<Score, String> createScoreColumn() {
        TableColumn<Score, String> scoreColumn = new TableColumn<Score, String>("SCORE");

        scoreColumn.setPrefWidth(100);
        scoreColumn.setStyle( "-fx-alignment: CENTER;");
        scoreColumn.setCellFactory(new Callback<TableColumn<Score, String>, TableCell<Score, String>>() {
            @Override
            public TableCell<Score, String> call(TableColumn<Score, String> param) {
                final TableCell<Score, String> cell = new TableCell<Score, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            int   selectedIndex  = getTableRow().getIndex();
                            Score selectedRecord = (Score) getItems().get(selectedIndex);
                            
                            Label 
                            score = new Label("" + selectedRecord.getValue());

                            setGraphic(score);
                        }
                    }
                };
                return cell;
            }
        });

        return scoreColumn;
    }
    @SuppressWarnings("unchecked")
	private void                       adjustSize(int _nbEntries, TableColumn<Score, ?> _adjustable, TableColumn<Score, ?>... _fixed) {
        setFixedCellSize(EntryHeight);

        ReadOnlyDoubleProperty[] widthProperties = new ReadOnlyDoubleProperty[_fixed.length];
    	for(int i = 0; i < _fixed.length; ++i)
    		widthProperties[i] = _fixed[i].widthProperty();

        Callable<Double>         widthBinding    = () -> {
        	double value = 0d;
        	for(int i = 0; i < _fixed.length; ++i)
        		value += _fixed[i].getWidth();

        	return value + 15;
        };

        DoubleBinding  bindings    = Bindings.createDoubleBinding(widthBinding, widthProperties);
        DoubleProperty otherWidths = new SimpleDoubleProperty();
        otherWidths.bind		(bindings);
        otherWidths.addListener	((_obs, _old, _new) -> _adjustable.setPrefWidth( getWidth() - _new.doubleValue() ));

        _adjustable.setResizable	(false);

        widthProperty()  	 . addListener((ov, t, t1) -> _adjustable.setPrefWidth( getWidth() - otherWidths.doubleValue() ));
        prefWidthProperty()  . set( 666 );
        maxWidthProperty()   . set( 666 );
        minHeightProperty()  . set((_nbEntries + 1) * EntryHeight + 2);
        prefHeightProperty() . set((_nbEntries + 1) * EntryHeight + 2);
        maxHeightProperty()  . set((_nbEntries + 1) * EntryHeight + 2);
    	
    }

}
