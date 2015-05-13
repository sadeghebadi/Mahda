package ir.rayacell.mahdaclient.manager;

import ir.rayacell.mahdaclient.model.BaseModel;
import ir.rayacell.mahdaclient.model.Command;
import ir.rayacell.mahdaclient.model.DeleteDownloadModel;
import ir.rayacell.mahdaclient.model.Direct3GModel;
import ir.rayacell.mahdaclient.model.DirectWIFIModel;
import ir.rayacell.mahdaclient.model.OrderStatusModel;
import ir.rayacell.mahdaclient.model.PingModel;
import ir.rayacell.mahdaclient.model.RestartModel;
import ir.rayacell.mahdaclient.model.ShowOnlineMapModel;
import ir.rayacell.mahdaclient.model.StatusModel;
import ir.rayacell.mahdaclient.param.BaseParam;

import java.util.ArrayList;

public class Parser {

	public static String baseParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		return commandType;
	}

	public static BaseModel flightParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String duration = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, Integer.parseInt(duration), 0, 0);

		return model;
	}

	public static BaseModel stringParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String duration = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String quality = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, Integer.parseInt(duration), 0, 0,
				Integer.parseInt(quality));

		return model;
	}

	public static BaseModel stringParserVoice(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String duration = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, Integer.parseInt(duration), 0, 0);

		return model;
	}
	public static BaseModel motionVideoParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String duration = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String quality = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		String ordertime = received_command.substring(star_index.get(6) + 1,
				star_index.get(7));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, Integer.parseInt(duration), 0, 0,
				Integer.parseInt(quality) ,ordertime);

		return model;
	}
	public static BaseModel photoStringParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String count = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String delay = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, 0, Integer.parseInt(count), Integer.parseInt(delay));

		return model;
	}
	public static BaseModel motionCaptureParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String count = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String delay = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		String orderTime = received_command.substring(star_index.get(6) + 1,
				star_index.get(7));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, 0, Integer.parseInt(count), Integer.parseInt(delay) , orderTime);

		return model;
	}
	public static BaseModel locationStringParser(String received_command) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < received_command.length(); i++) {
			if (received_command.charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(received_command.substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = received_command.substring(star_index.get(1) + 1,
				star_index.get(2));
		String phonenumber = received_command.substring(star_index.get(2) + 1,
				star_index.get(3));
		String datetime = received_command.substring(star_index.get(3) + 1,
				star_index.get(4));
		String duration = received_command.substring(star_index.get(4) + 1,
				star_index.get(5));
		String delay = received_command.substring(star_index.get(5) + 1,
				star_index.get(6));
		BaseModel model = new Command(command_id, phonenumber, commandType,
				datetime, Integer.parseInt(duration), 0,
				Integer.parseInt(delay));

		return model;
	}

	public static BaseModel setConnectionParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		String ip_address = param.getCommand().substring(star_index.get(3) + 1,
				star_index.get(4));
		String current_time = param.getCommand().substring(
				star_index.get(4) + 1, star_index.get(5));
		BaseModel model = new StatusModel(command_id, phone_number,
				commandType, ip_address, current_time);
		return model;
	}

	public static BaseModel baseModelParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));

		BaseModel model = new BaseModel(command_id, phone_number, commandType);
		return model;
	}

	public static BaseModel deleteDownloadParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		String file_name = param.getCommand().substring(star_index.get(3) + 1,
				star_index.get(4));

		BaseModel model = new DeleteDownloadModel(command_id, phone_number,
				commandType, file_name);
		return model;
	}

	public static BaseModel mapParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		/*
		 * String ip_address = param.getCommand().substring(star_index.get(3) +
		 * 1, star_index.get(4));
		 */

		BaseModel model = new ShowOnlineMapModel(command_id, phone_number,
				commandType /* , ip_address */);
		return model;
	}

	public static BaseModel wifiParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));

		/*
		 * String ip_address = param.getCommand().substring(star_index.get(3) +
		 * 1, star_index.get(4));
		 */

		BaseModel model = new DirectWIFIModel(command_id, phone_number,
				commandType);
		return model;
	}

	public static BaseModel _3gParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));

		String ip_address = param.getCommand().substring(star_index.get(4) + 1,
				star_index.get(5));
		NetworkManager.set3Gserver(ip_address);

		BaseModel model = new Direct3GModel(command_id, phone_number,
				commandType, ip_address);
		return model;
	}
	public static BaseModel setPingParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		BaseModel model = new PingModel(command_id, phone_number,
				commandType);
		return model;
	}
	public static BaseModel setRestartParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		BaseModel model = new RestartModel(command_id, phone_number,
				commandType);
		return model;
	}
	public static BaseModel orderStatusParser(BaseParam param) {
		ArrayList<Integer> star_index = new ArrayList<Integer>();
		for (int i = 0; i < param.getCommand().length(); i++) {
			if (param.getCommand().charAt(i) == '*') {
				star_index.add(i);
			}
		}
		long command_id = Long.parseLong(param.getCommand().substring(
				star_index.get(0) + 1, star_index.get(1)));
		String commandType = param.getCommand().substring(
				star_index.get(1) + 1, star_index.get(2));
		String phone_number = param.getCommand().substring(
				star_index.get(2) + 1, star_index.get(3));
		String status = param.getCommand().substring(star_index.get(3) + 1,
				star_index.get(4));
		/*
		 * String ip_address = param.getCommand().substring(star_index.get(3) +
		 * 1, star_index.get(4));
		 */

		BaseModel model = new OrderStatusModel(command_id, phone_number,
				commandType /* , ip_address */, status);
		return model;
	}
}
