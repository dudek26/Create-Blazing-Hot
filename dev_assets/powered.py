import json
from pathlib import Path

models = Path("models/")
for entry in models.iterdir():
    if (entry.is_dir()):
        submodels = entry.iterdir()
        for subentry in submodels:
            if (subentry.is_file() & subentry.name.endswith(".json") & (not subentry.name.endswith("_powered.json"))):
                path = "models/" + entry.name + "/" + subentry.name

                model = json.loads(open(path).read())
                texture = model["textures"]["1"].replace("{color}", "{color}_powered")
                model["textures"]["1"] = texture
                model["textures"]["particle"] = texture

                modelName = entry.name.replace(".json", "")
                generatedModels = Path("generated/powered/" + modelName + "/")
                generatedModels.mkdir(parents=True, exist_ok=True)
                f = open("generated/powered/" + modelName + "/" + subentry.name.replace(".json", "_powered.json"), "w")
                f.write(json.dumps(model))
                f.close()
