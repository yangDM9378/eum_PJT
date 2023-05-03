# pip install fastapi
# pip install uvicorn
# pip install python-multipart
from fastapi import FastAPI, File, UploadFile

app = FastAPI()
@app.get("/")
async def root():
    return {"result": True}

@app.post("/pose")
async def root(image1: UploadFile = File(...), image2: UploadFile = File(...)):
    # # 이미지 파일을 받아와서 Pillow를 사용하여 Image 객체로 변환합니다.
    # image1 = Image.open(io.BytesIO(await image1.read()))
    # image2 = Image.open(io.BytesIO(await image2.read()))

    print(image1)
    print(image2)
    # Mediapipe 모델을 사용하여 이미지를 처리합니다.
    # 이 부분은 Mediapipe 모델의 사용 방법에 따라 다를 수 있습니다.
    # result = run_mediapipe(image1, image2)
    result= True
    # 처리 결과를 JSON 형태로 반환합니다.
    return {"result": result}