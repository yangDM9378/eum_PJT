# pip install fastapi
# pip install uvicorn
# pip install python-multipart

# pip install mediapipe
# pip install pandas
# pip freeze > requirements.txt

# 가상환경 켜기
# source venv/Scripts/activate
# pip install -r requirements.txt
# uvicorn main:pose_api --reload 
from fastapi import FastAPI, File, UploadFile
import mediapipe as mp
import cv2
import matplotlib.pyplot as plt
import math
import numpy as np
from starlette.middleware.cors import CORSMiddleware
# from same_pose import makeLandmark,calculateAngle,classifyPose



pose_api = FastAPI()

origins = [
    "http://localhost",
    "http://localhost:3000",

]
pose_api.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@pose_api.get("/")
async def root():
    return {"message": "Hello World"}


@pose_api.post("/pose")
async def checkPose(image1: UploadFile = File(...), image2: UploadFile = File(...)):
    # read the uploaded files as bytes
    image1_bytes = await image1.read()
    image2_bytes = await image2.read()
    
    # convert the bytes to OpenCV format
    base_image = cv2.imdecode(np.frombuffer(image1_bytes, np.uint8), cv2.IMREAD_COLOR)
    input_image = cv2.imdecode(np.frombuffer(image2_bytes, np.uint8), cv2.IMREAD_COLOR)
    
    mp_pose = mp.solutions.pose
    pose = mp_pose.Pose()

    # Initializing mediapipe drawing class, useful for annotation.
    # mediapipe의 drawing class를 초기화한다.
    mp_drawing = mp.solutions.drawing_utils

    # plt 이미지 크기 설정
    plt.figure(figsize = [10, 10])
    plt.title("Sample Image");plt.axis('off');plt.imshow(base_image[:,:,::-1]);plt.show()

    # plt 이미지 크기 설정
    plt.figure(figsize = [10, 10])
    plt.title("Sample Image");plt.axis('off');plt.imshow(input_image[:,:,::-1]);plt.show()

    # # 이미지 rgb로 변환
    # base_image_rgb = cv2.cvtColor(base_image, cv2.COLOR_BGR2RGB)
    # input_image_rgb = cv2.cvtColor(input_image, cv2.COLOR_BGR2RGB)

    # pose detection
    result1 = pose.process(base_image_rgb)
    result2 = pose.process(input_image_rgb)

    # # 랜드마크를 그릴 사진을 COPY한다.
    # img_copy = base_image.copy()
 
    # # 랜드마크를 찾는다.
    # if result1.pose_landmarks:
        
    #     # sample image에 landmark를 그린다.
    #     mp_drawing.draw_landmarks(image=img_copy, landmark_list=result1.pose_landmarks, connections=mp_pose.POSE_CONNECTIONS)
        
    #     # figure의 크기를 설정한다.
    #     fig = plt.figure(figsize = [10, 10])
    
    #     # landmark가 draw된 image를 보여주기 전에 BGR TO RGB를 위해 copy_image의 순서를 반대로 변형해준다. 
    #     plt.title("Output");plt.axis('off');plt.imshow(img_copy[:,:,::-1]);plt.show()


    # # 랜드마크를 그릴 사진을 COPY한다.
    # img_copy2 = input_image.copy()
    
    # # 랜드마크를 찾는다.
    # if result1.pose_landmarks:
        
    #     # sample image에 landmark를 그린다.
    #     mp_drawing.draw_landmarks(image=img_copy2, landmark_list=result2.pose_landmarks, connections=mp_pose.POSE_CONNECTIONS)
        
    #     # figure의 크기를 설정한다.
    #     fig = plt.figure(figsize = [10, 10])
    
    #     # landmark가 draw된 image를 보여주기 전에 BGR TO RGB를 위해 copy_image의 순서를 반대로 변형해준다. 
    #     plt.title("Output");plt.axis('off');plt.imshow(img_copy2[:,:,::-1]);plt.show()

    # mp_drawing.plot_landmarks(result1.pose_world_landmarks, mp_pose.POSE_CONNECTIONS)
    # mp_drawing.plot_landmarks(result2.pose_world_landmarks, mp_pose.POSE_CONNECTIONS)

    # landmark를 원하는 모양으로 변경
    base_landmark = makeLandmark(result1.pose_landmarks.landmark, base_image)
    input_landmark = makeLandmark(result2.pose_landmarks.landmark, input_image)

    result = classifyPose(base_landmark, input_landmark)
    testresult = '땡'
    if result:
       testresult = '정답'
    return {"message": testresult}

    def makeLandmark(src_landmark, image) :
        # detection landmarks를 저장할 빈 list 초기화
        dest_landmark = []

        height, width, _ = image.shape

        # landmark가 감지 되었는지 확인
        if src_landmark:

            # 감지된 landmark 반복
            for landmark in src_landmark:

                # landmark를 list에 추가하기
                dest_landmark.append((int(landmark.x * width), int(landmark.y * height), (landmark.z * width)))
        
        return dest_landmark

    # 앵글 계산 함수
    def calculateAngle(landmark1, landmark2, landmark3):

        # Get the required landmarks coordinates.
        x1, y1, _ = landmark1
        x2, y2, _ = landmark2
        x3, y3, _ = landmark3

        # Calculate the angle between the three points
        angle = math.degrees(math.atan2(y3 - y2, x3 - x2) - math.atan2(y1 - y2, x1 - x2))
        
        # Check if the angle is less than zero.
        if angle < 0:

            # Add 360 to the found angle.
            angle += 360
        
        # Return the calculated angle.
        return angle

    def classifyPose(base_landmark, input_landmark):
        isSamePose = False

        approximationError = 360 * 0.05

        print(approximationError)

        # Calculate the required angles.
        #----------------------------------------------------------------------------------------------------------------
        
        # Get the angle between the left shoulder, elbow and wrist points. 
        # 11번, 13번, 15번 landmark 
        # 왼쪽 어깨, 왼쪽 팔꿈치, 왼쪽 손목 landmark angle 값 계산 
        left_elbow_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.LEFT_SHOULDER.value],
                                        base_landmark[mp_pose.PoseLandmark.LEFT_ELBOW.value],
                                        base_landmark[mp_pose.PoseLandmark.LEFT_WRIST.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.LEFT_SHOULDER.value],
                                        input_landmark[mp_pose.PoseLandmark.LEFT_ELBOW.value],
                                        input_landmark[mp_pose.PoseLandmark.LEFT_WRIST.value])
        
        # 12번, 14번, 16번 landmark 
        # 오른쪽 어깨, 오른쪽 팔꿈치, 오른쪽 손목 landmark angle 값 계산 
        right_elbow_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                        base_landmark[mp_pose.PoseLandmark.RIGHT_ELBOW.value],
                                        base_landmark[mp_pose.PoseLandmark.RIGHT_WRIST.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                        input_landmark[mp_pose.PoseLandmark.RIGHT_ELBOW.value],
                                        input_landmark[mp_pose.PoseLandmark.RIGHT_WRIST.value])
        
        # 13번, 15번, 23번 landmark 
        # 왼쪽 어깨, 왼쪽 팔꿈치, 왼쪽 엉덩이, landmark angle 값 계산 
        left_shoulder_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.LEFT_ELBOW.value],
                                            base_landmark[mp_pose.PoseLandmark.LEFT_SHOULDER.value],
                                            base_landmark[mp_pose.PoseLandmark.LEFT_HIP.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.LEFT_ELBOW.value],
                                            input_landmark[mp_pose.PoseLandmark.LEFT_SHOULDER.value],
                                            input_landmark[mp_pose.PoseLandmark.LEFT_HIP.value])

        # 12번, 14번, 24번 landmark 
        # 오른쪽 어깨, 오른쪽 팔꿈치, 오른쪽 엉덩이 landmark angle 값 계산  
        right_shoulder_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.RIGHT_HIP.value],
                                            base_landmark[mp_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                            base_landmark[mp_pose.PoseLandmark.RIGHT_ELBOW.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.RIGHT_HIP.value],
                                            input_landmark[mp_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                            input_landmark[mp_pose.PoseLandmark.RIGHT_ELBOW.value])

        # 23번, 25번, 27번 landmark 
        # 왼쪽 엉덩이, 왼쪽 무릎, 왼쪽 발목 landmark angle 값 계산 
        left_knee_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.LEFT_HIP.value],
                                        base_landmark[mp_pose.PoseLandmark.LEFT_KNEE.value],
                                        base_landmark[mp_pose.PoseLandmark.LEFT_ANKLE.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.LEFT_HIP.value],
                                        input_landmark[mp_pose.PoseLandmark.LEFT_KNEE.value],
                                        input_landmark[mp_pose.PoseLandmark.LEFT_ANKLE.value])

        # 24번, 26번, 28번 landmark 
        # 오른쪽 엉덩이, 오른쪽 무릎, 오른쪽 발목  landmark angle 값 계산 
        right_knee_angle_diff = calculateAngle(base_landmark[mp_pose.PoseLandmark.RIGHT_HIP.value],
                                        base_landmark[mp_pose.PoseLandmark.RIGHT_KNEE.value],
                                        base_landmark[mp_pose.PoseLandmark.RIGHT_ANKLE.value]) - calculateAngle(input_landmark[mp_pose.PoseLandmark.RIGHT_HIP.value],
                                        input_landmark[mp_pose.PoseLandmark.RIGHT_KNEE.value],
                                        input_landmark[mp_pose.PoseLandmark.RIGHT_ANKLE.value])

        print(left_elbow_angle_diff)
        print(right_elbow_angle_diff)
        print(left_shoulder_angle_diff)
        print(right_shoulder_angle_diff)
        print(left_knee_angle_diff)
        print(right_knee_angle_diff)
        
        return isSamePose