package net.trpfrog.frogrobo.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.trpfrog.frogrobo.streaming.MentionListener;
import net.trpfrog.frogrobo.streaming.TrpFrogUserStream;
import net.trpfrog.frogrobo.streaming.TweetStream;

public class MainFrame{
	private static JFrame frame = new JFrame();

	private MainFrame() {
	}

	/**
	 *
	 * GUIです
	 * @author つまみ <Twitter:@TrpFrog>
	 * deprecated 新しくGUIにJavaFXを採用したのでSwingで書かれたこのクラスは非推奨です。
	 *
	 */

	//@Deprecated
	public static void createFrame() {

		frame.setSize(200, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true); // Can't resize.
		frame.requestFocus();
		frame.setLocationByPlatform(true);

		JPanel titleTextPanel = new JPanel();
		titleTextPanel.setLayout(new BoxLayout(titleTextPanel,
				BoxLayout.PAGE_AXIS));

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,
				BoxLayout.PAGE_AXIS));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		JLabel titleText = new JLabel();
		titleText.setText("FrogRobo");
		titleText.setFont(new Font(null, Font.BOLD, 25));
		titleText.setHorizontalAlignment(JLabel.CENTER);
		titleTextPanel.add(titleText, BorderLayout.CENTER);

		List<MentionListener> listenerList = new ArrayList<>();
		listenerList.addAll(TweetStream.getInstance().getMentionListenerList());
		listenerList.addAll(TrpFrogUserStream.getInstance().getListenerList());

		JButton applyButton = new JButton();
		JButton checkboxTogglerButton = new JButton();
		JCheckBox[] commandsCheckBoxes = new JCheckBox[listenerList.size()];
		int i = 0;
		for (MentionListener listener : listenerList) {
			commandsCheckBoxes[i] = new JCheckBox();
			commandsCheckBoxes[i].setText(listener.getCommandName());
			commandsCheckBoxes[i].setToolTipText(listener.getCommandDescription());
			commandsCheckBoxes[i].setSelected(true);
			commandsCheckBoxes[i].addActionListener(action->{
				applyButton.setEnabled(true);
				checkboxTogglerButton.setText("Deselect all");
			});
			checkBoxPanel.add(commandsCheckBoxes[i], BorderLayout.CENTER);
			i++;
		}

//		JButton applyButton = new JButton(); (上で宣言済み)
		applyButton.setText("Apply");
		applyButton.setEnabled(false);
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = 0;
				for (JCheckBox checkbox : commandsCheckBoxes) {
					listenerList.get(i).setDisable(checkbox.isSelected());
					i++;
				}
				applyButton.setEnabled(false);
			}
		});
		buttonPanel.add(applyButton, BorderLayout.CENTER);

//		JButton checkboxTogglerButton = new JButton(); (上で宣言済み)
		checkboxTogglerButton.setText("Deselect all");
		checkboxTogglerButton.addActionListener(action -> {
			boolean allChecked = true;
			for (JCheckBox checkbox : commandsCheckBoxes) {
				if (checkbox.isSelected() == false)
					continue;
				allChecked = false;
			}
			for (JCheckBox checkbox : commandsCheckBoxes) {
				checkbox.setSelected(allChecked);
			}
			if (allChecked) {
				checkboxTogglerButton.setText("Deselect all");
			} else {
				checkboxTogglerButton.setText("Select all");
			}
		});
		buttonPanel.add(checkboxTogglerButton, BorderLayout.CENTER);

		frame.add(titleTextPanel, BorderLayout.NORTH);
		frame.add(checkBoxPanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

}