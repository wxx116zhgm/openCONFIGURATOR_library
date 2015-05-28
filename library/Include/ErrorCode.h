/************************************************************************
\file ErrorCode.h
\brief Implementation of the Enumeration ErrorCode
\author rueckerc, Bernecker+Rainer Industrie Elektronik Ges.m.b.H.
\date 01-May-2015 12:00:00
************************************************************************/

/*------------------------------------------------------------------------------
Copyright (c) 2015, Bernecker+Rainer Industrie-Elektronik Ges.m.b.H. (B&R)
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the copyright holders nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDERS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
------------------------------------------------------------------------------*/
#if !defined ERROR_CODE_H
#define ERROR_CODE_H

#include <cstdint>
#include "Constants.h"

namespace IndustrialNetwork
{
	namespace POWERLINK
	{
		namespace Core
		{
			namespace ErrorHandling
			{
				/**
				\brief
				\author rueckerc
				*/
				DLLEXPORT enum class ErrorCode : std::uint8_t
				{

					SUCCESS = 0,// < Operation completed successfully.
					FILE_WRITE_FAILED, // < Cannot write to file.
					FILE_READ_FAILED, // < Cannot read from file.
					LEGACY_ERROR, //< Deprecated error occured in operation.
					SUBOBJECT_INVALID, //< Invalid subindex encountered during operation.
					OBJECT_INVALID, //< Invalid index encountered during operation.
					NODEID_INVALID, //< Invalid node id encountered during operation.
					ATTRIBUTEVALUE_INVALID, //< Invalid attribute value encountered during operation.
					ATTRIBUTEVALUE_NOT_IN_RANGE, //< Attribute value not in valid range.
					UNSUPPORTED_ATTRIBUTETYPE, //< Attribute type not supported by operation.
					NO_NODES_CONFIGURED, //< Network does not contain any nodes.
					NO_CONTROLLED_NODES_CONFIGURED, //< Network does not contain any controlled nodes.
					NO_MANAGING_NODE_CONFIGURED, //< Network does not contain a managing node.
					OD_EMPTY, //< Object dictionary is empty.
					OBJECT_CONTAINS_NO_SUBOBJECTS, //< %Index does not contain subindices.
					NODE_DOES_NOT_EXIST, //< %Node does not exist in the network.
					OBJECT_DOES_NOT_EXIST, //< %Index does not exist on %Node.
					SUBOBJECT_DOES_NOT_EXIST, //< %SubIndex does not exist in %Index.
					NODE_EXISTS, //< %Node already exists in the network.
					OBJECT_EXISTS, //< %Index already exists on a %Node.
					SUBOBJECT_EXISTS, //< %SubIndex already exists for an %Index.
					EXTERNAL_SYSTEM_CALL_FAILED, //< External system call failed during operation.
					TPDO_CHANNEL_COUNT_EXCEEDED, //< Max. no. of TPDO-Channels exceeded.
					NODE_CONFIGURATION_ERROR, //< %Node configuration error occured.
					MAPPED_OBJECT_DOES_NOT_EXIST, // < A mapped %Index does not exist on %Node.
					MAPPED_SUBOBJECT_DOES_NOT_EXIST, //< A mapped %SubIndex does not exist on %Node.
					INSUFFICIENT_MAPPING_OBJECTS, // < Insufficient mapping objects on the %Node.
					PARAMETER_NOT_FOUND, //< %Parameter not found on a %Node.
					STRUCT_DATATYPE_NOT_FOUND, // < Struct datatype not found on a %Node.
					SIMPLE_DATATYPE_NOT_FOUND, // < Simple datatype not found on the %Node.
					VALUE_NOT_WITHIN_RANGE, // < Value not within a valid range.
					CHANNEL_PAYLOAD_LIMIT_EXCEEDED, // < Max. Channel-Payload exceeded.
					CHANNEL_OBJECT_LIMIT_EXCEEDED, // < Max. no. of Channel-Objects exceeded.
					PDO_DATATYPE_INVALID, // < PDO datatype invalid.
					UNSUPPORTED_PI_GEN_LANGUAGE, // < Process image generation not supported for given programming language.
					MAX_PI_SIZE_EXCEEDED, // < Maximum size of process image exceeded.
					MULTIPLEXING_NOT_SUPPORTED, // < Multiplexing not supported.
					MULTIPLEX_CYCLE_ASSIGN_INVALID, //< Invalid multiplexed cycle assigned to %Node.
					OBJECT_LIMITS_INVALID, // < High-/Lowlimit of the object are invalid.
					LOW_CN_PRES_TIMEOUT, // < CNPResTimeout (0x1F92) is too low.
					CROSS_TRAFFIC_STATION_LIMIT_EXCEEDED, //< CN exceeds the no. of allowed cross-traffic stations (device description entry D_PDO_RPDOChannels_U16).
					ARGUMENT_INVALID_NULL, // < Invalid function-argument (NULL).
					ARGUMENT_INVALID_EMPTY, // < Invalid function-argument (empty).
					UNHANDLED_EXCEPTION, //< Unhandled exception occurred during operation.
					NO_DEFAULT_OR_ACTUAL_VALUE, // < No default or actual value defined for object.
					FEATURE_VALUE_NOT_FOUND, //< CN-/MN- or GeneralFeature not found in a node's XDD.
					PLKDATATYPE_SIZE_UNDEFINED, //The given POWERLINK data type has no defined size.
					OBJECT_SIZE_MAPPED_INVALID, // < Mapped object size invalid.
					MAPPING_TYPE_FOR_PDO_INVALID, // < Mapped object has an invalid/incompatible PDOmapping.
					ACCESS_TYPE_FOR_PARAMETER_INVALID, // < A mapped object's referenced %Parameter has an invalid/incompatible accessType.
					ACCESS_TYPE_FOR_PDO_INVALID, // < A mapped object has an invalid/incompatible accessType.
					PDO_OFFSET_INVALID, // < A mapped object has an invalid offset (non-contigous).
					XML_INVALID, // < Processed XML file not schema-valid.
					SCHEMA_NOT_FOUND_OR_WELL_FORMED, // < XML schema not found or not well-formed.
					SCHEMA_INVALID, // < XML schema not valid.
					SCHEMA_PARSER_CONTEXT_ERROR, // < XML parser context error.
					SCHEMA_VALIDATION_CONTEXT_ERROR, // < XML schema validation context error.
					NO_PROJECT_LOADED, // < No project loaded.
					MAPPING_INVALID, // < An enabled mapping entry is empty.
					PARAMETER_VALUE_NOT_SET, // < A specific parameter value is not set.
					PARAMETER_VALUE_INVALID, // < A value is invalid for a specific parameter.
					PATH_EXISTS, // < A path already exists in the project configuration.
					PATH_DOES_NOT_EXIST, // < A path does not exists in the project configuration.
					VIEW_SETTING_EXISTS, // < A view setting exists in the project configuration.
					VIEW_SETTING_DOES_NOT_EXIST, // < A view setting does not exist in the project configuration.
					VIEW_SETTINGS_DOES_NOT_EXIST, // < A view settings group does not exist in the project configuration.
					BUILD_SETTING_EXISTS, // < An auto generation setting already exists in the project configuration.
					BUILD_SETTING_DOES_NOT_EXIST, // < An auto generation setting does not exist in the project configuration.
					BUILD_CONFIGURATION_EXISTS, // < An auto generation settings group does already exist in the project configuration.
					BUILD_CONFIGURATION_DOES_NOT_EXIST, // < An auto generation settings group does not exist in the project configuration.
					BUILD_CONFIGURATION_IS_ACTIVE, // < The build configuration group is set active in the project configuration.
					NETWORK_EXISTS,
					NETWORK_DOES_NOT_EXIST
				};

			}

		}

	}

}
#endif
