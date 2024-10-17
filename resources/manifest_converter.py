import os
import json

def parse_id(category):
    parsed_criteria = []
    for resource, criteria in category.items():
        points = []
        args = []

        if "MOST" in criteria:
            args.append(criteria.split(" ")[1])
            points.append(int(criteria.split("=")[-1].strip()))
            criteria_name = "PointsIfHasMostOfResource"
        
        elif "FEWEST" in criteria:
            args.append(criteria.split(" ")[1])
            points.append(int(criteria.split("=")[-1].strip()))
            criteria_name = "PointsIfHasFewestOfResource"
        
        elif "EVEN" in criteria:
            even_points = int(criteria.split("EVEN=")[1].split(",")[0].strip())
            odd_points = int(criteria.split("ODD=")[1].strip())
            args.append(criteria.split(":")[0])
            points.extend([even_points, odd_points])
            criteria_name = "PointsIfHasEvenOrOddOfResource"
        
        elif "/" in criteria:
            separated_rules = criteria.split(',')
            for sep_rule in separated_rules:
                points.append(int(sep_rule.split("/")[0].strip()))
                args.append(sep_rule.split("/")[1].strip())
            criteria_name = "PointsPerCopyOfResource"
        
        elif "+" in criteria:
            first_split = criteria.split('=')
            args = [x.strip() for x in first_split[0].split("+")]
            points.append(int(first_split[1]))
            criteria_name = "PointsPerCombinationOfResources"
        
        else:
            print(f"Unknown rule format: {criteria}")
            continue
        
        parsed_criteria.append({
            "Resource": resource,
            "Criteria": {
                "Name": criteria_name,
                "Points": points,
                "Args": args
            },
        })
    
    return parsed_criteria


def parse_special_id(category):
    parsed_criteria = []
    for resource, criteria in category.items():
        points = []
        args = []
    
        if "MOST TOTAL VEGETABLE" in criteria:
            points.append(int(criteria.split("=")[-1].strip()))
            criteria_name = "PointsIfMostTotalResources"
        elif "FEWEST TOTAL VEGETABLE" in criteria:
            points.append(int(criteria.split("=")[-1].strip()))
            criteria_name = "PointsIfFewestTotalResources"
        elif "VEGETABLE TYPE >=" in criteria:
            points.append(int(criteria.split("/")[0].strip()))
            args.append(int(criteria.split(">=")[-1].strip()))
            criteria_name = "PointsPerTypeIfHasAtLeastXOfType"
        elif "MISSING VEGETABLE TYPE" in criteria:
            points.append(int(criteria.split("/")[0].strip()))
            criteria_name = "PointsPerMissingType"
        elif "COMPLETE SET" in criteria:
            points.append(int(criteria.split("=")[-1].strip()))
            criteria_name = "PointsIfHasCompleteSet"
        else:
            print(f"Unknown rule format: {criteria}")

        parsed_criteria.append({
            "Resource": resource,
            "Criteria": {
                "Name": criteria_name,
                "Points": points,
                "Args": args
            },
        })

    return parsed_criteria


def convert_json(input_json):
    new_format = {"Cards": []}
    
    for category in input_json['cards']:
        if int(category['id']) < 18: # Simpler to do it manually
            new_format["Cards"].extend(parse_id(category['criteria']))
        elif int(category['id']) == 18:
            new_format["Cards"].extend(parse_special_id(category['criteria']))
    
    return new_format

# Get current working directory
here = os.path.dirname(os.path.realpath(__file__))

# Load the original JSON file
with open(os.path.join(here,'OldPointSaladManifest.json'), 'r', encoding='utf-8') as file:
    data = json.load(file)

# Convert the data
converted_data = convert_json(data)

# Save the converted data to a new JSON file
with open(os.path.join(here,'PointSaladManifest.json'), 'w', encoding='utf-8') as file:
    json.dump(converted_data, file, indent=2)

print("Conversion complete. Check 'converted.json' for the result. \
      Don't forget to add the special rules")
