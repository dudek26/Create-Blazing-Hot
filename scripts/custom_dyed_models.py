import json
from pathlib import Path

colors = ["white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink"]

generated = Path("generated/")
models = Path("models/")
for entry in models.iterdir():
    if (entry.is_dir()):
        submodels = entry.iterdir()
        for subentry in submodels:
            if (subentry.is_file() & subentry.name.endswith(".json")):
                path = "models/" + entry.name + "/" + subentry.name
                
                for color in colors:
                    model = json.loads(open(path).read())
                    texture = model["textures"]["0"].replace("{color}", color)
                    model["textures"]["0"] = texture
                    model["textures"]["particle"] = texture
                
                    modelName = entry.name.replace(".json", "")
                    generatedModels = Path("generated/" + modelName +"/")
                    generatedModels.mkdir(parents = True, exist_ok = True)
                    f = open("generated/" + modelName + "/" + subentry.name.replace("color", color), "w")
                    f.write(json.dumps(model))
                    f.close()
    
    
