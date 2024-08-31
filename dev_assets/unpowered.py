import json
from pathlib import Path

models = Path("models/")
for entry in models.iterdir():
    if (entry.is_dir()):
        submodels = entry.iterdir()
        for subentry in submodels:
            if (subentry.is_file() & subentry.name.endswith(".json") & subentry.name.endswith("_powered.json")):
                path = "models/" + entry.name + "/" + subentry.name

                model = json.loads(open(path).read())
                texture = model["textures"]["1"].replace("_powered", "")
                model["textures"]["1"] = texture
                model["textures"]["particle"] = texture

                modelName = entry.name.replace(".json", "")
                generatedModels = Path("generated/unpowered/" + modelName + "/")
                generatedModels.mkdir(parents=True, exist_ok=True)
                f = open("generated/unpowered/" + modelName + "/" + subentry.name.replace("_powered", ""), "w")
                f.write(json.dumps(model))
                f.close()
