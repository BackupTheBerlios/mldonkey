package net.mldonkey.g2gui.view;

import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.download.FileInfoTableContentProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableLabelProvider;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DownloadTab
	extends G2guiTab
	implements Observer, Runnable, DisposeListener, SelectionListener, MouseListener {

	static final int[] COLUMN_WIDTHS = { 25, 300, 40, 70, 70, 40, 70 };
	ResourceBundle bundle = ResourceBundle.getBundle("g2gui");
	TableColumn[] columns;
	FileInfoIntMap fileInfoMap;

	TableViewer table;
	TableItem popupItem;
	Menu popupMenu;
	MenuItem pauseItem,resumeItem,cancelItem,renameItem,linkItem,fakeItem;
	
	public DownloadTab(Gui gui) {
		super(gui);
		toolItem.setText(bundle.getString("TT_Button"));
		toolItem.setImage(
			Gui.createTransparentImage(
				new Image(
					toolItem.getParent().getDisplay(),
					"src/icons/transfer2.png"),
				toolItem.getParent()));
		createContents(this.content);

		gui.getCore().addObserver(this);
	}

	protected void createContents(Composite parent) {
		table =
			new TableViewer(
				parent,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.getTable().setLayoutData(
			new GridData(
				GridData.FILL_BOTH
					| GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL));
		table.setContentProvider(new FileInfoTableContentProvider());
		table.setLabelProvider(new FileInfoTableLabelProvider());
		table.getTable().addMouseListener(this);
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
		columns = new TableColumn[7];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new TableColumn(table.getTable(), SWT.LEFT);
			columns[i].setText(bundle.getString("TT_Field" + i));
			columns[i].setWidth(COLUMN_WIDTHS[i]);
			columns[i].addDisposeListener(this);
			columns[i].addSelectionListener(this);
		}
		
		popupMenu = new Menu(parent.getShell(),SWT.POP_UP);
		
		pauseItem = new MenuItem(popupMenu,SWT.PUSH);
		pauseItem.setText(bundle.getString("TT_Menu0"));
		pauseItem.addSelectionListener(this);
		resumeItem = new MenuItem(popupMenu,SWT.PUSH);
		resumeItem.setText(bundle.getString("TT_Menu1"));
		resumeItem.addSelectionListener(this);
		cancelItem = new MenuItem(popupMenu,SWT.PUSH);
		cancelItem.setText(bundle.getString("TT_Menu2"));
		cancelItem.addSelectionListener(this);
		renameItem = new MenuItem(popupMenu,SWT.PUSH);
		renameItem.setText(bundle.getString("TT_Menu3"));
		renameItem.addSelectionListener(this);
		linkItem = new MenuItem(popupMenu,SWT.PUSH);
		linkItem.setText(bundle.getString("TT_Menu4"));
		linkItem.addSelectionListener(this);
		fakeItem = new MenuItem(popupMenu,SWT.PUSH);
		fakeItem.setText(bundle.getString("TT_Menu5"));
		fakeItem.addSelectionListener(this);		
		
		table.getTable().setMenu(popupMenu);
		parent.addDisposeListener(this);
	}

	public void mouseDoubleClick(MouseEvent arg0) {}

	public void mouseDown(MouseEvent arg0) {
		if(arg0.button==3) {
			popupItem = table.getTable().getItem(new Point(arg0.x,arg0.y));
		}
	}

	public void mouseUp(MouseEvent arg0) {}

	public void run() {
		if(table.getInput()==null) table.setInput(fileInfoMap);
		if(fileInfoMap.size()==table.getTable().getItemCount() && fileInfoMap.getId()!=0 && fileInfoMap.contains(fileInfoMap.getId())) table.update(fileInfoMap.get(fileInfoMap.getId()),null);
		else table.refresh();
	}

	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof FileInfoIntMap) {
			if(this.fileInfoMap==null) this.fileInfoMap = (FileInfoIntMap)arg1;
			table.getTable().getDisplay().asyncExec(this);
		}
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO sort-routine
		if(arg0.widget instanceof MenuItem) {
			MenuItem item = (MenuItem)arg0.widget;
			FileInfo file = (FileInfo)popupItem.getData();
			if(item==pauseItem) file.setState(EnumFileState.PAUSED);
			if(item==resumeItem) file.setState(EnumFileState.DOWNLOADING);
			if(item==cancelItem) file.setState(EnumFileState.CANCELLED);
			if(item==linkItem) {
				Clipboard clipBoard = new Clipboard(item.getDisplay());
				String link = "ed2k://|file|"+file.getName()+"|"+file.getSize()+"|"+file.getMd4()+"|/";
				clipBoard.setContents(new Object[]{link},new Transfer[] {TextTransfer.getInstance()});
				clipBoard.dispose();
			}
			if(item==fakeItem) {
				String link = "ed2k://|file|"+file.getName()+"|"+file.getSize()+"|"+file.getMd4()+"|/";
				Program.findProgram(".htm").execute("http://edonkeyfakes.ath.cx/fakecheck/update/fakecheck.php?ed2k="+link);
			}
		}
	}

	public void widgetDisposed(DisposeEvent arg0) {
		// TODO store Column position
	}

	public void widgetSelected(SelectionEvent arg0) {
		widgetDefaultSelected(arg0);
	}

}
