import json
from pathlib import Path

models = Path("models/")
for entry in models.iterdir():
    if (entry.is_dir()):
        submodels = entry.iterdir()
        for subentry in submodels:
            if (subentry.is_file() & subentry.name.endswith(".json")):
                path = "models/" + entry.name + "/" + subentry.name

                model = json.loads(open(path).read())
                if ("white" not in model["textures"]["1"]):
                    continue;
                texture = model["textures"]["1"].replace("white", "{color}")
                model["textures"]["1"] = texture
                model["textures"]["particle"] = texture

                f = open(path, "w")
                f.write(json.dumps(model))
                f.close()
