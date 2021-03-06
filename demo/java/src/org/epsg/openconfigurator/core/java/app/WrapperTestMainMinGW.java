package org.epsg.openconfigurator.core.java.app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.epsg.openconfigurator.lib.wrapper.AccessType;
import org.epsg.openconfigurator.lib.wrapper.ByteCollection;
import org.epsg.openconfigurator.lib.wrapper.CNFeatureEnum;
import org.epsg.openconfigurator.lib.wrapper.IEC_Datatype;
import org.epsg.openconfigurator.lib.wrapper.MNFeatureEnum;
import org.epsg.openconfigurator.lib.wrapper.MapIterator;
import org.epsg.openconfigurator.lib.wrapper.ObjectCollection;
import org.epsg.openconfigurator.lib.wrapper.ObjectType;
import org.epsg.openconfigurator.lib.wrapper.OpenConfiguratorCore;
import org.epsg.openconfigurator.lib.wrapper.PDOMapping;
import org.epsg.openconfigurator.lib.wrapper.ParameterAccess;
import org.epsg.openconfigurator.lib.wrapper.PlkDataType;
import org.epsg.openconfigurator.lib.wrapper.Result;
import org.epsg.openconfigurator.lib.wrapper.SettingsCollection;

public class WrapperTestMainMinGW {

	// Load openCONFIGURATOR core libraries
	static {
		System.loadLibrary("libboost_date_time-mgw48-mt-1_54");
		System.loadLibrary("libboost_system-mgw48-mt-1_54");
		System.loadLibrary("libboost_chrono-mgw48-mt-1_54");
		System.loadLibrary("libboost_filesystem-mgw48-mt-1_54");
		System.loadLibrary("libboost_thread-mgw48-mt-1_54");
		System.loadLibrary("libboost_log-mgw48-mt-1_54");
		System.loadLibrary("libboost_log_setup-mgw48-mt-1_54");
		System.loadLibrary("libopenconfigurator_core_lib");
		System.loadLibrary("libopenconfigurator_core_wrapper_java");
	}

	public static void main(String[] args) {
		// Retrieve main managing class of the library
		OpenConfiguratorCore core = OpenConfiguratorCore.GetInstance();

		// Init logger class with configuration file path
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("boost_log_settings.ini"));

			String config = new String(encoded, Charset.defaultCharset());
			core.InitLoggingConfiguration(config);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create an initial network
		core.CreateNetwork("test");

		// Add two nodes to the network
		core.CreateNode("test", (short) 240, "MN");

		// Create settings configuration
		/*
		 * <AutoGenerationSettings id="GenerateAll"> <Setting
		 * name="MN_MAPPING_FOR_ALL_NODES" enabled="true"/> <Setting
		 * name="MN_NODE_ASSIGNMENT_FOR_ALL_NODES" enabled="true"/> <Setting
		 * name="MN_PRES_TIMEOUT_FOR_ALL_NODES" enabled="true"/>
		 * </AutoGenerationSettings>
		 */
		core.CreateConfiguration("test", "all");
		core.CreateConfigurationSetting("test", "all", "GENERATE_MN_MAPPING_FOR_NODES", "");

		// Create specific settings configuration
		/*
		 * <AutoGenerationSettings id="SpecificConfiguration"> <Setting
		 * name="MN_MAPPING_FOR_NODE" value="1" enabled="true"/> <Setting
		 * name="MN_NODE_ASSIGNMENT_FOR_NODE" value="1" enabled="true"/>
		 * <Setting name="MN_PRES_TIMEOUT_FOR_NODE" value="1" enabled="true"/>
		 * </AutoGenerationSettings>
		 */
		core.CreateConfiguration("test", "custom");
		core.CreateConfigurationSetting("test", "custom", "GENERATE_MN_MAPPING_FOR_NODES", "1");

		SettingsCollection coll = new SettingsCollection();
		System.out.println(core.GetConfigurationSettings("test", "all", coll).GetErrorType());
		System.out.println(coll.size());
		System.out.println(core.SetConfigurationSettingEnabled("test", "all", "GENERATE_MN_MAPPING_FOR_NODES", false)
				.GetErrorType());

		core.SetActiveConfiguration("test", "custom");

		core.CreateConfiguration("test", "none");

		Result cycleTime = core.CreateObject("test", (short) 240, 0x1006, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "400", "1000");
		System.out.println(cycleTime.GetErrorType());
		System.out.println(cycleTime.GetErrorMessage());

		core.CreateObject("test", (short) 240, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "400", "214");

		core.CreateObject("test", (short) 240, 0x1301, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 240, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 240, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "10", "");
		core.CreateSubObject("test", (short) 240, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "1500", "300");
		core.CreateSubObject("test", (short) 240, 0x1F98, (short) 9, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "2", "10");
		core.CreateSubObject("test", (short) 240, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "", "");

		core.SetSubObjectLimits("test", (short) 240, 0x1f98, (short) 0x9, "", "");

		core.CreateObject("test", (short) 240, 0x1F81, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 240, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F81, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 240, 0x1F8B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8B, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 240, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 240, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 240, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F9B, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 240, 0x1F92, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "5", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 240, 0x1F92, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");

		core.CreateObject("test", (short) 240, 0x1600, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "3");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0x0010000000202104", "");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 240, 0x1600, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 240, 0x1F26, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "4", "4");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F26, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 240, 0x1F27, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "4", "4");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 240, 0x1F27, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateNode("test", (short) 241, "RMN");
		core.CreateObject("test", (short) 241, 0x1006, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "400", "10000");

		core.CreateObject("test", (short) 241, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 241, 0x1301, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 241, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F9B, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 241, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "10", "");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "300", "");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 9, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "2", "");

		core.CreateObject("test", (short) 241, 0x1F81, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 0xF0, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F81, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 241, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 0xF0, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 241, 0x1F8B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 240, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 241, 0x1F8B, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 241, 0x1F92, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "5", "5");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 240, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");
		core.CreateSubObject("test", (short) 241, 0x1F92, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "25000", "");

		core.CreateObject("test", (short) 241, 0x1600, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1600, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "3");
		core.CreateSubObject("test", (short) 241, 0x1600, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 241, 0x1600, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 241, 0x1600, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 241, 0x1F26, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "4", "4");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F26, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 241, 0x1F27, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "4", "4");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 10, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 20, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1F27, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 241, 0x1020, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 241, 0x1020, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 241, 0x1020, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 241, 0x1020, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "25000", "25000");
		core.CreateSubObject("test", (short) 241, 0x1F98, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.SetCycleTime("test", 10000); // Set Cycle time

		core.CreateNode("test", (short) 10, "SuperControlledNode");
		core.CreateObject("test", (short) 10, 0x1020, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1020, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 10, 0x1020, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1020, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 10, 0x1C0B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1C0B, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 10, 0x1C0C, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1C0C, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 10, 0x1C0D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1C0D, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");

		core.CreateObject("test", (short) 10, 0x1F81, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 240, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F81, (short) 241, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 10, 0x1400, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1400, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 10, 0x1400, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 10, 0x1400, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");

		core.CreateObject("test", (short) 10, 0x1600, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1600, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "3");
		core.CreateSubObject("test", (short) 10, 0x1600, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 10, 0x1600, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 10, 0x1600, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 10, 0x1800, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1800, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 10, 0x1800, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 10, 0x1800, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");

		core.CreateObject("test", (short) 10, 0x1A00, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1A00, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "3");
		core.CreateSubObject("test", (short) 10, 0x1A00, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 10, 0x1A00, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");
		core.CreateSubObject("test", (short) 10, 0x1A00, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED64,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 10, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F9B, (short) 241, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 10, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		// core.CreateObject("test", (short) 10, 0x1301, ObjectType.VAR,
		// "testName", PlkDataType.UNSIGNED32, AccessType.RW,
		// PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 10, 0x1006, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "10000");

		core.CreateObject("test", (short) 10, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 10, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 10, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateSubObject("test", (short) 10, 0x1F98, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");

		core.CreateNode("test", (short) 20, "FieldbusBoy");
		core.CreateObject("test", (short) 20, 0x1020, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1020, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 20, 0x1020, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1020, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 20, 0x1C0B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1C0B, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 20, 0x1C0C, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1C0C, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 20, 0x1C0D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1C0D, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");

		core.CreateObject("test", (short) 20, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F9B, (short) 241, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 20, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 20, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");
		core.CreateObject("test", (short) 20, 0x1301, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 20, 0x1006, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "10000");

		core.CreateObject("test", (short) 20, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 9, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateSubObject("test", (short) 20, 0x1F98, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");

		core.CreateNode("test", (short) 1, "FieldbusGirl");
		core.CreateObject("test", (short) 1, 0x1020, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1020, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 1, 0x1020, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1020, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 1, 0x1C0B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1C0B, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 1, 0x1C0C, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1C0C, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "100");
		core.CreateObject("test", (short) 1, 0x1C0D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1C0D, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "10");

		core.CreateObject("test", (short) 1, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F9B, (short) 241, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 1, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 1, 0x1006, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "10000");

		core.CreateObject("test", (short) 1, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 1, 0x1301, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		core.SetObjectActualValue("test", (short) 1, 0x1006, "100000", true);

		core.CreateObject("test", (short) 1, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 9, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 1, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateNode("test", (short) 5, "node");
		core.CreateObject("test", (short) 5, 0x1F98, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 4, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 7, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 8, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 9, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 5, 0x1F9B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F9B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 5, 0x1300, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 5, 0x1301, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "5000");

		core.CreateObject("test", (short) 5, 0x1F8D, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 0xF0, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8D, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 5, 0x1C0B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1C0B, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "20");

		core.CreateSubObject("test", (short) 10, 0x1C0D, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "");
		core.CreateObject("test", (short) 5, 0x1F8B, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 0xF0, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F8B, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 5, 0x1F81, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 0, ObjectType.VAR, "testName", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.DEFAULT, "3", "3");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 1, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 2, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 10, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 20, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 0xF0, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F81, (short) 0xF1, ObjectType.VAR, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateObject("test", (short) 5, 0x1006, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32, AccessType.RW,
				PDOMapping.DEFAULT, "400", "10000");

		core.CreateObject("test", (short) 5, 0x1020, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1020, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "2", "2");
		core.CreateSubObject("test", (short) 5, 0x1020, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1020, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0");

		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 3, ObjectType.VAR, "testName", PlkDataType.UNSIGNED32,
				AccessType.RW, PDOMapping.DEFAULT, "0", "0");
		core.CreateSubObject("test", (short) 5, 0x1F98, (short) 5, ObjectType.VAR, "testName", PlkDataType.UNSIGNED16,
				AccessType.RW, PDOMapping.DEFAULT, "36", "");

		core.CreateObject("test", (short) 5, 0x2100, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.DEFAULT, "0", "10");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED16, AccessType.RW, PDOMapping.DEFAULT, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED24, AccessType.RW, PDOMapping.DEFAULT, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 3, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED32, AccessType.RW, PDOMapping.DEFAULT, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 4, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED40, AccessType.RW, PDOMapping.DEFAULT, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 6, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED56, AccessType.RW, PDOMapping.NO, "", "100");
		core.CreateSubObject("test", (short) 5, 0x2100, (short) 7, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED64, AccessType.RW, PDOMapping.NO, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 5, 0x2102, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 0, ObjectType.DEFTYPE, "testName",
				PlkDataType.UNSIGNED8, AccessType.RW, PDOMapping.NO, "0", "7");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 1, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER16, AccessType.RW, PDOMapping.NO, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 2, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER24, AccessType.RW, PDOMapping.NO, "0", "0x100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 3, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER32, AccessType.RW, PDOMapping.NO, "", "100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 4, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER40, AccessType.RW, PDOMapping.NO, "", "100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 5, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER48, AccessType.RW, PDOMapping.NO, "", "100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 6, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER56, AccessType.RW, PDOMapping.NO, "", "100");
		core.CreateSubObject("test", (short) 5, 0x2102, (short) 7, ObjectType.DEFTYPE, "testName",
				PlkDataType.INTEGER64, AccessType.RW, PDOMapping.NO, "0", "0x0010000000202104");

		core.CreateObject("test", (short) 5, 0x2110, ObjectType.DEFTYPE, "Name", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x00, ObjectType.DEFTYPE, "Name", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.RPDO, "0", "15");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x0d, ObjectType.DEFTYPE, "Name",
				PlkDataType.UNICODE_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x01, ObjectType.DEFTYPE, "Name",
				PlkDataType.VISIBLE_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x02, ObjectType.DEFTYPE, "Name",
				PlkDataType.OCTET_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x03, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "0x64646464");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x0b, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "254.254.254.254");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x0c, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "1.1.1.1");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x04, ObjectType.DEFTYPE, "Name", PlkDataType.BOOLEAN,
				AccessType.RW, PDOMapping.RPDO, "false", "true");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x05, ObjectType.DEFTYPE, "Name",
				PlkDataType.MAC_ADDRESS, AccessType.RW, PDOMapping.RPDO, "0", "0x000A086121012");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x06, ObjectType.DEFTYPE, "Name", PlkDataType.REAL32,
				AccessType.RW, PDOMapping.RPDO, "0", "1267.43233E12");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x07, ObjectType.DEFTYPE, "Name", PlkDataType.REAL32,
				AccessType.RW, PDOMapping.RPDO, "0", "1.234532432");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x08, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "0", "-1E4");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x09, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "0", "12.78e-2");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x0a, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "6", "-0");
		core.CreateSubObject("test", (short) 5, 0x2110, (short) 0x10, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "6", "-0");

		core.CreateObject("test", (short) 5, 0x2300, ObjectType.DEFTYPE, "Name", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x00, ObjectType.DEFTYPE, "Name", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.RPDO, "0", "5");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x0d, ObjectType.DEFTYPE, "Name",
				PlkDataType.UNICODE_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x01, ObjectType.DEFTYPE, "Name",
				PlkDataType.VISIBLE_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x02, ObjectType.DEFTYPE, "Name",
				PlkDataType.OCTET_STRING, AccessType.RW, PDOMapping.RPDO, "100", "WriteThisString");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x03, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "0x64646464");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x0b, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "254.254.254.254");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x0c, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "1.1.1.1");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x04, ObjectType.DEFTYPE, "Name", PlkDataType.BOOLEAN,
				AccessType.RW, PDOMapping.RPDO, "false", "true");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x05, ObjectType.DEFTYPE, "Name",
				PlkDataType.MAC_ADDRESS, AccessType.RW, PDOMapping.RPDO, "0", "0x000A086121012");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x06, ObjectType.DEFTYPE, "Name", PlkDataType.REAL32,
				AccessType.RW, PDOMapping.RPDO, "0", "1267432.33E12");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x07, ObjectType.DEFTYPE, "Name", PlkDataType.REAL32,
				AccessType.RW, PDOMapping.RPDO, "0", "1.234532432");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x08, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "0", "-1E4");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x09, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "0", "12.78e-2");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x0a, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "6", "-0");
		core.CreateSubObject("test", (short) 5, 0x2300, (short) 0x10, ObjectType.DEFTYPE, "Name", PlkDataType.REAL64,
				AccessType.RW, PDOMapping.RPDO, "6", "-0");

		core.CreateObject("test", (short) 5, 0x1400, ObjectType.DEFTYPE, "Name", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x1400, (short) 0x00, ObjectType.DEFTYPE, "Name", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.RPDO, "1", "1");
		core.CreateSubObject("test", (short) 5, 0x1400, (short) 0x01, ObjectType.DEFTYPE, "Name", PlkDataType.UNSIGNED8,
				AccessType.RW, PDOMapping.RPDO, "0", "0");

		core.CreateObject("test", (short) 5, 0x2000, ObjectType.RECORD, "objectName", PlkDataType.UNDEFINED,
				AccessType.UNDEFINED, PDOMapping.UNDEFINED, "", "");
		core.CreateSubObject("test", (short) 5, 0x2000, (short) 0x0b, ObjectType.DEFTYPE, "Name",
				PlkDataType.IP_ADDRESS, AccessType.RW, PDOMapping.RPDO, "100", "254.254.254.254");

		Result test = core.CreateParameter("test", (short) 1, "UID_DOM_Index2100_Sub1E", "UID_DT_Index2100_Sub1E",
				ParameterAccess.read);
		System.out.println(test.IsSuccessful());
		test = core.CreateStructDatatype("test", (short) 1, "notFound", "Index2100_Sub1E");
		test = core.CreateStructDatatype("test", (short) 1, "UID_DT_Index2100_Sub1E", "Index2100_Sub1E");
		System.out.println(test.IsSuccessful());
		test = core.CreateVarDeclaration("test", (short) 1, "notFound", "Index2100_Sub1E_OK", "NetworkStatus",
				IEC_Datatype.USINT);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2100_Sub1E", "Index2100_Sub1E_OK",
				"NetworkStatus", IEC_Datatype.USINT);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2100_Sub1E",
				"UID_Index2100_Sub1E_StatusInput01", "StatusInput01", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2100_Sub1E",
				"UID_Index2100_Sub1E_Bit_Unused_01", "StatusInput01", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2100_Sub1E",
				"UID_Index2100_Sub1E_StatusInput02", "StatusInput01", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2100_Sub1E",
				"UID_Index2100_Sub1E_Bit_Unused_02", "StatusInput01", IEC_Datatype.BITSTRING, 5);
		System.out.println(test.IsSuccessful());

		test = core.CreateParameter("test", (short) 1, "UID_DOM_Index2101_Sub1E", "UID_DT_Index2101_Sub1E",
				ParameterAccess.read);
		System.out.println(test.IsSuccessful());
		test = core.CreateStructDatatype("test", (short) 1, "UID_DT_Index2101_Sub1E", "Index2101_Sub1E");
		System.out.println(test.IsSuccessful());
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E", "Index2101_Sub1E_OK",
				"NetworkStatus", IEC_Datatype.USINT);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput01", "DigitalInput01", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput02", "DigitalInput02", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput03", "DigitalInput03", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput04", "DigitalInput04", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput05", "DigitalInput05", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput06", "DigitalInput06", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput07", "DigitalInput07", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput08", "DigitalInput08", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput09", "DigitalInput09", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput10", "DigitalInput10", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput11", "DigitalInput11", IEC_Datatype.BITSTRING, 1);
		test = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_DigitalInput12", "DigitalInput12", IEC_Datatype.BITSTRING, 1);
		Result res = core.CreateVarDeclaration("test", (short) 1, "UID_DT_Index2101_Sub1E",
				"UID_Index2101_Sub1E_Bit_Unused_01", "Bit_Unused_01", IEC_Datatype.BITSTRING, 4);
		System.out.println(test.IsSuccessful());
		test = core.CreateParameter("test", (short) 1, "UID_100", ParameterAccess.read, IEC_Datatype.UDINT);

		long[] size = new long[2];
		res = core.GetDatatypeSize("test", (short) 1, "UID_DT_Index2100_Sub1E", size);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		System.out.println(size);

		res = core.GetDatatypeSize("test", (short) 1, "UID_DT_Index2101_Sub1E", size);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		System.out.println(size);

		res = core.GetDatatypeSize("test", (short) 1, "UID_100", size);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		System.out.println(size);

		res = core.GetDatatypeSize("test", (short) 1, "notFound", size);
		System.out.println(test.IsSuccessful());
		System.out.println(size);

		res = core.CreateDomainObject("test", (short) 1, 0x2000, ObjectType.DEFSTRUCT, "domain", PDOMapping.DEFAULT,
				"UID_DOM_Index2101_Sub1E");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		long[] obsize = new long[1];
		res = core.GetObjectSize("test", (short) 1, 0x2000, obsize);
		System.out.println(obsize);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		res = core.CreateDomainSubObject("test", (short) 1, 0x2000, (short) 0x1, ObjectType.DEFSTRUCT, "domain", PDOMapping.DEFAULT,
				"UID_DOM_Index2101_Sub1E");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		res = core.GetSubObjectSize("test", (short) 1, 0x2000, (short) 0x1, obsize);
		System.out.println(obsize);
		System.out.println(res.IsSuccessful());

		res = core.SetFeatureValue("test", (short) 240, MNFeatureEnum.DLLMNFeatureMultiplex, "true");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		res = core.SetFeatureValue("test", (short) 240, MNFeatureEnum.DLLMNPResChaining, "true");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		res = core.SetFeatureValue("test", (short) 5, CNFeatureEnum.DLLCNPResChaining, "true");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		res = core.SetOperationModeChained("test", (short) 5);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		res = core.SetFeatureValue("test", (short) 10, CNFeatureEnum.DLLCNFeatureMultiplex, "true");
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		res = core.SetOperationModeMultiplexed("test", (short) 10, (short) 5);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());

		String[] tempString = new String[2];
		ByteCollection collection = new ByteCollection();
		res = core.BuildConfiguration("test", tempString, collection);
		System.out.println(res.GetErrorType());
		System.out.println(res.GetErrorMessage());
		System.out.println(tempString);
		System.console().readLine();

		ObjectCollection objectCollection = new ObjectCollection();
		core.GetObjectsWithActualValue("test", (short) 240, objectCollection);
		for (MapIterator iterator = objectCollection.iterator(); iterator.hasNext();) {
			String actualValue = (String) iterator.next();
			System.out.println(actualValue);
			iterator.GetKey().getFirst(); // index
			iterator.GetKey().getSecond(); // subindex
		}

		res = core.RemoveNode("test", (short) 1);
		System.out.println(res.IsSuccessful());

		res = core.BuildConfiguration("test", tempString, collection);
		System.out.println(tempString);
		System.out.println(res.IsSuccessful());
	}

}
