


function editUser(){


return(
    <></>
);
}

export default editUser;


const [avatarPreview, setAvatarPreview] = useState('/avatars/default.png');



/*<Box
 display='flex'
 textAlign='center'
 justifyContent='center'
 flexDirection='column'>

 <ImageAvatar size='md' src={avatarPreview || user?.avatar} />

 <Button
   variant='contained'
   component='label'
   startIcon={<CloudUploadIcon />}>
   Choose Avatar
   <input
     name='avatar'
     accept='image/*'
     id='contained-button-file'
     type='file'
     hidden
     onChange={(e) => {
       const fileReader = new FileReader();
       fileReader.onload = () => {
         if (fileReader.readyState === 2) {
           setFieldValue('avatar', fileReader.result);
           setAvatarPreview(fileReader.result);
         }
       };
       fileReader.readAsDataURL(e.target.files[0]);
     }}
   />
 </Button>
</Box>*/